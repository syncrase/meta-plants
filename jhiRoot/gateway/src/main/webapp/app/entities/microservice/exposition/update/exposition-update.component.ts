import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IExposition, Exposition } from '../exposition.model';
import { ExpositionService } from '../service/exposition.service';
import { IPlante } from 'app/entities/microservice/plante/plante.model';
import { PlanteService } from 'app/entities/microservice/plante/service/plante.service';

@Component({
  selector: 'gp-exposition-update',
  templateUrl: './exposition-update.component.html',
})
export class ExpositionUpdateComponent implements OnInit {
  isSaving = false;

  plantesSharedCollection: IPlante[] = [];

  editForm = this.fb.group({
    id: [],
    valeur: [],
    ensoleilement: [],
    plante: [],
  });

  constructor(
    protected expositionService: ExpositionService,
    protected planteService: PlanteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exposition }) => {
      this.updateForm(exposition);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const exposition = this.createFromForm();
    if (exposition.id !== undefined) {
      this.subscribeToSaveResponse(this.expositionService.update(exposition));
    } else {
      this.subscribeToSaveResponse(this.expositionService.create(exposition));
    }
  }

  trackPlanteById(index: number, item: IPlante): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExposition>>): void {
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

  protected updateForm(exposition: IExposition): void {
    this.editForm.patchValue({
      id: exposition.id,
      valeur: exposition.valeur,
      ensoleilement: exposition.ensoleilement,
      plante: exposition.plante,
    });

    this.plantesSharedCollection = this.planteService.addPlanteToCollectionIfMissing(this.plantesSharedCollection, exposition.plante);
  }

  protected loadRelationshipsOptions(): void {
    this.planteService
      .query()
      .pipe(map((res: HttpResponse<IPlante[]>) => res.body ?? []))
      .pipe(map((plantes: IPlante[]) => this.planteService.addPlanteToCollectionIfMissing(plantes, this.editForm.get('plante')!.value)))
      .subscribe((plantes: IPlante[]) => (this.plantesSharedCollection = plantes));
  }

  protected createFromForm(): IExposition {
    return {
      ...new Exposition(),
      id: this.editForm.get(['id'])!.value,
      valeur: this.editForm.get(['valeur'])!.value,
      ensoleilement: this.editForm.get(['ensoleilement'])!.value,
      plante: this.editForm.get(['plante'])!.value,
    };
  }
}
