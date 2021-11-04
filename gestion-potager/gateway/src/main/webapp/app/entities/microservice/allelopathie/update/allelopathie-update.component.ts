import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAllelopathie, Allelopathie } from '../allelopathie.model';
import { AllelopathieService } from '../service/allelopathie.service';
import { IPlante } from 'app/entities/microservice/plante/plante.model';
import { PlanteService } from 'app/entities/microservice/plante/service/plante.service';

@Component({
  selector: 'gp-allelopathie-update',
  templateUrl: './allelopathie-update.component.html',
})
export class AllelopathieUpdateComponent implements OnInit {
  isSaving = false;

  plantesSharedCollection: IPlante[] = [];
  ciblesCollection: IPlante[] = [];
  originesCollection: IPlante[] = [];

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required]],
    description: [],
    cible: [],
    origine: [],
    interaction: [],
  });

  constructor(
    protected allelopathieService: AllelopathieService,
    protected planteService: PlanteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ allelopathie }) => {
      this.updateForm(allelopathie);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const allelopathie = this.createFromForm();
    if (allelopathie.id !== undefined) {
      this.subscribeToSaveResponse(this.allelopathieService.update(allelopathie));
    } else {
      this.subscribeToSaveResponse(this.allelopathieService.create(allelopathie));
    }
  }

  trackPlanteById(index: number, item: IPlante): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAllelopathie>>): void {
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

  protected updateForm(allelopathie: IAllelopathie): void {
    this.editForm.patchValue({
      id: allelopathie.id,
      type: allelopathie.type,
      description: allelopathie.description,
      cible: allelopathie.cible,
      origine: allelopathie.origine,
      interaction: allelopathie.interaction,
    });

    this.plantesSharedCollection = this.planteService.addPlanteToCollectionIfMissing(
      this.plantesSharedCollection,
      allelopathie.interaction
    );
    this.ciblesCollection = this.planteService.addPlanteToCollectionIfMissing(this.ciblesCollection, allelopathie.cible);
    this.originesCollection = this.planteService.addPlanteToCollectionIfMissing(this.originesCollection, allelopathie.origine);
  }

  protected loadRelationshipsOptions(): void {
    this.planteService
      .query()
      .pipe(map((res: HttpResponse<IPlante[]>) => res.body ?? []))
      .pipe(
        map((plantes: IPlante[]) => this.planteService.addPlanteToCollectionIfMissing(plantes, this.editForm.get('interaction')!.value))
      )
      .subscribe((plantes: IPlante[]) => (this.plantesSharedCollection = plantes));

    this.planteService
      .query({ filter: 'allelopathie-is-null' })
      .pipe(map((res: HttpResponse<IPlante[]>) => res.body ?? []))
      .pipe(map((plantes: IPlante[]) => this.planteService.addPlanteToCollectionIfMissing(plantes, this.editForm.get('cible')!.value)))
      .subscribe((plantes: IPlante[]) => (this.ciblesCollection = plantes));

    this.planteService
      .query({ filter: 'allelopathie-is-null' })
      .pipe(map((res: HttpResponse<IPlante[]>) => res.body ?? []))
      .pipe(map((plantes: IPlante[]) => this.planteService.addPlanteToCollectionIfMissing(plantes, this.editForm.get('origine')!.value)))
      .subscribe((plantes: IPlante[]) => (this.originesCollection = plantes));
  }

  protected createFromForm(): IAllelopathie {
    return {
      ...new Allelopathie(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      description: this.editForm.get(['description'])!.value,
      cible: this.editForm.get(['cible'])!.value,
      origine: this.editForm.get(['origine'])!.value,
      interaction: this.editForm.get(['interaction'])!.value,
    };
  }
}
