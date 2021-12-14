import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClassification, Classification } from '../classification.model';
import { ClassificationService } from '../service/classification.service';
import { IRaunkierPlante } from 'app/entities/plantsMS/raunkier-plante/raunkier-plante.model';
import { RaunkierPlanteService } from 'app/entities/plantsMS/raunkier-plante/service/raunkier-plante.service';
import { ICronquistPlante } from 'app/entities/plantsMS/cronquist-plante/cronquist-plante.model';
import { CronquistPlanteService } from 'app/entities/plantsMS/cronquist-plante/service/cronquist-plante.service';
import { IAPGIPlante } from 'app/entities/plantsMS/apgi-plante/apgi-plante.model';
import { APGIPlanteService } from 'app/entities/plantsMS/apgi-plante/service/apgi-plante.service';
import { IAPGIIPlante } from 'app/entities/plantsMS/apgii-plante/apgii-plante.model';
import { APGIIPlanteService } from 'app/entities/plantsMS/apgii-plante/service/apgii-plante.service';
import { IAPGIIIPlante } from 'app/entities/plantsMS/apgiii-plante/apgiii-plante.model';
import { APGIIIPlanteService } from 'app/entities/plantsMS/apgiii-plante/service/apgiii-plante.service';
import { IAPGIVPlante } from 'app/entities/plantsMS/apgiv-plante/apgiv-plante.model';
import { APGIVPlanteService } from 'app/entities/plantsMS/apgiv-plante/service/apgiv-plante.service';

@Component({
  selector: 'perma-classification-update',
  templateUrl: './classification-update.component.html',
})
export class ClassificationUpdateComponent implements OnInit {
  isSaving = false;

  raunkiersCollection: IRaunkierPlante[] = [];
  cronquistsCollection: ICronquistPlante[] = [];
  apg1sCollection: IAPGIPlante[] = [];
  apg2sCollection: IAPGIIPlante[] = [];
  apg3sCollection: IAPGIIIPlante[] = [];
  apg4sCollection: IAPGIVPlante[] = [];

  editForm = this.fb.group({
    id: [],
    raunkier: [],
    cronquist: [],
    apg1: [],
    apg2: [],
    apg3: [],
    apg4: [],
  });

  constructor(
    protected classificationService: ClassificationService,
    protected raunkierPlanteService: RaunkierPlanteService,
    protected cronquistPlanteService: CronquistPlanteService,
    protected aPGIPlanteService: APGIPlanteService,
    protected aPGIIPlanteService: APGIIPlanteService,
    protected aPGIIIPlanteService: APGIIIPlanteService,
    protected aPGIVPlanteService: APGIVPlanteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classification }) => {
      this.updateForm(classification);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classification = this.createFromForm();
    if (classification.id !== undefined) {
      this.subscribeToSaveResponse(this.classificationService.update(classification));
    } else {
      this.subscribeToSaveResponse(this.classificationService.create(classification));
    }
  }

  trackRaunkierPlanteById(index: number, item: IRaunkierPlante): number {
    return item.id!;
  }

  trackCronquistPlanteById(index: number, item: ICronquistPlante): number {
    return item.id!;
  }

  trackAPGIPlanteById(index: number, item: IAPGIPlante): number {
    return item.id!;
  }

  trackAPGIIPlanteById(index: number, item: IAPGIIPlante): number {
    return item.id!;
  }

  trackAPGIIIPlanteById(index: number, item: IAPGIIIPlante): number {
    return item.id!;
  }

  trackAPGIVPlanteById(index: number, item: IAPGIVPlante): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassification>>): void {
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

  protected updateForm(classification: IClassification): void {
    this.editForm.patchValue({
      id: classification.id,
      raunkier: classification.raunkier,
      cronquist: classification.cronquist,
      apg1: classification.apg1,
      apg2: classification.apg2,
      apg3: classification.apg3,
      apg4: classification.apg4,
    });

    this.raunkiersCollection = this.raunkierPlanteService.addRaunkierPlanteToCollectionIfMissing(
      this.raunkiersCollection,
      classification.raunkier
    );
    this.cronquistsCollection = this.cronquistPlanteService.addCronquistPlanteToCollectionIfMissing(
      this.cronquistsCollection,
      classification.cronquist
    );
    this.apg1sCollection = this.aPGIPlanteService.addAPGIPlanteToCollectionIfMissing(this.apg1sCollection, classification.apg1);
    this.apg2sCollection = this.aPGIIPlanteService.addAPGIIPlanteToCollectionIfMissing(this.apg2sCollection, classification.apg2);
    this.apg3sCollection = this.aPGIIIPlanteService.addAPGIIIPlanteToCollectionIfMissing(this.apg3sCollection, classification.apg3);
    this.apg4sCollection = this.aPGIVPlanteService.addAPGIVPlanteToCollectionIfMissing(this.apg4sCollection, classification.apg4);
  }

  protected loadRelationshipsOptions(): void {
    this.raunkierPlanteService
      .query({ filter: 'classification-is-null' })
      .pipe(map((res: HttpResponse<IRaunkierPlante[]>) => res.body ?? []))
      .pipe(
        map((raunkierPlantes: IRaunkierPlante[]) =>
          this.raunkierPlanteService.addRaunkierPlanteToCollectionIfMissing(raunkierPlantes, this.editForm.get('raunkier')!.value)
        )
      )
      .subscribe((raunkierPlantes: IRaunkierPlante[]) => (this.raunkiersCollection = raunkierPlantes));

    this.cronquistPlanteService
      .query({ filter: 'classification-is-null' })
      .pipe(map((res: HttpResponse<ICronquistPlante[]>) => res.body ?? []))
      .pipe(
        map((cronquistPlantes: ICronquistPlante[]) =>
          this.cronquistPlanteService.addCronquistPlanteToCollectionIfMissing(cronquistPlantes, this.editForm.get('cronquist')!.value)
        )
      )
      .subscribe((cronquistPlantes: ICronquistPlante[]) => (this.cronquistsCollection = cronquistPlantes));

    this.aPGIPlanteService
      .query({ filter: 'classification-is-null' })
      .pipe(map((res: HttpResponse<IAPGIPlante[]>) => res.body ?? []))
      .pipe(
        map((aPGIPlantes: IAPGIPlante[]) =>
          this.aPGIPlanteService.addAPGIPlanteToCollectionIfMissing(aPGIPlantes, this.editForm.get('apg1')!.value)
        )
      )
      .subscribe((aPGIPlantes: IAPGIPlante[]) => (this.apg1sCollection = aPGIPlantes));

    this.aPGIIPlanteService
      .query({ filter: 'classification-is-null' })
      .pipe(map((res: HttpResponse<IAPGIIPlante[]>) => res.body ?? []))
      .pipe(
        map((aPGIIPlantes: IAPGIIPlante[]) =>
          this.aPGIIPlanteService.addAPGIIPlanteToCollectionIfMissing(aPGIIPlantes, this.editForm.get('apg2')!.value)
        )
      )
      .subscribe((aPGIIPlantes: IAPGIIPlante[]) => (this.apg2sCollection = aPGIIPlantes));

    this.aPGIIIPlanteService
      .query({ filter: 'classification-is-null' })
      .pipe(map((res: HttpResponse<IAPGIIIPlante[]>) => res.body ?? []))
      .pipe(
        map((aPGIIIPlantes: IAPGIIIPlante[]) =>
          this.aPGIIIPlanteService.addAPGIIIPlanteToCollectionIfMissing(aPGIIIPlantes, this.editForm.get('apg3')!.value)
        )
      )
      .subscribe((aPGIIIPlantes: IAPGIIIPlante[]) => (this.apg3sCollection = aPGIIIPlantes));

    this.aPGIVPlanteService
      .query({ filter: 'classification-is-null' })
      .pipe(map((res: HttpResponse<IAPGIVPlante[]>) => res.body ?? []))
      .pipe(
        map((aPGIVPlantes: IAPGIVPlante[]) =>
          this.aPGIVPlanteService.addAPGIVPlanteToCollectionIfMissing(aPGIVPlantes, this.editForm.get('apg4')!.value)
        )
      )
      .subscribe((aPGIVPlantes: IAPGIVPlante[]) => (this.apg4sCollection = aPGIVPlantes));
  }

  protected createFromForm(): IClassification {
    return {
      ...new Classification(),
      id: this.editForm.get(['id'])!.value,
      raunkier: this.editForm.get(['raunkier'])!.value,
      cronquist: this.editForm.get(['cronquist'])!.value,
      apg1: this.editForm.get(['apg1'])!.value,
      apg2: this.editForm.get(['apg2'])!.value,
      apg3: this.editForm.get(['apg3'])!.value,
      apg4: this.editForm.get(['apg4'])!.value,
    };
  }
}
