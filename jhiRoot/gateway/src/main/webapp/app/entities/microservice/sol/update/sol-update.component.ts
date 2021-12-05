import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISol, Sol } from '../sol.model';
import { SolService } from '../service/sol.service';
import { IPlante } from 'app/entities/microservice/plante/plante.model';
import { PlanteService } from 'app/entities/microservice/plante/service/plante.service';

@Component({
  selector: 'gp-sol-update',
  templateUrl: './sol-update.component.html',
})
export class SolUpdateComponent implements OnInit {
  isSaving = false;

  plantesSharedCollection: IPlante[] = [];

  editForm = this.fb.group({
    id: [],
    phMin: [],
    phMax: [],
    type: [],
    richesse: [],
    plante: [],
  });

  constructor(
    protected solService: SolService,
    protected planteService: PlanteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sol }) => {
      this.updateForm(sol);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sol = this.createFromForm();
    if (sol.id !== undefined) {
      this.subscribeToSaveResponse(this.solService.update(sol));
    } else {
      this.subscribeToSaveResponse(this.solService.create(sol));
    }
  }

  trackPlanteById(index: number, item: IPlante): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISol>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(sol: ISol): void {
    this.editForm.patchValue({
      id: sol.id,
      phMin: sol.phMin,
      phMax: sol.phMax,
      type: sol.type,
      richesse: sol.richesse,
      plante: sol.plante,
    });

    this.plantesSharedCollection = this.planteService.addPlanteToCollectionIfMissing(this.plantesSharedCollection, sol.plante);
  }

  protected loadRelationshipsOptions(): void {
    this.planteService
      .query()
      .pipe(map((res: HttpResponse<IPlante[]>) => res.body ?? []))
      .pipe(map((plantes: IPlante[]) => this.planteService.addPlanteToCollectionIfMissing(plantes, this.editForm.get('plante')!.value)))
      .subscribe((plantes: IPlante[]) => (this.plantesSharedCollection = plantes));
  }

  protected createFromForm(): ISol {
    return {
      ...new Sol(),
      id: this.editForm.get(['id'])!.value,
      phMin: this.editForm.get(['phMin'])!.value,
      phMax: this.editForm.get(['phMax'])!.value,
      type: this.editForm.get(['type'])!.value,
      richesse: this.editForm.get(['richesse'])!.value,
      plante: this.editForm.get(['plante'])!.value,
    };
  }
}
