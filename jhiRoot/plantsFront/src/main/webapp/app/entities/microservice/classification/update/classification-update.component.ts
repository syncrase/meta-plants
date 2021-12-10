import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClassification, Classification } from '../classification.model';
import { ClassificationService } from '../service/classification.service';
import { IRaunkier } from 'app/entities/microservice/raunkier/raunkier.model';
import { RaunkierService } from 'app/entities/microservice/raunkier/service/raunkier.service';
import { ICronquist } from 'app/entities/microservice/cronquist/cronquist.model';
import { CronquistService } from 'app/entities/microservice/cronquist/service/cronquist.service';
import { IAPGI } from 'app/entities/microservice/apgi/apgi.model';
import { APGIService } from 'app/entities/microservice/apgi/service/apgi.service';
import { IAPGII } from 'app/entities/microservice/apgii/apgii.model';
import { APGIIService } from 'app/entities/microservice/apgii/service/apgii.service';
import { IAPGIII } from 'app/entities/microservice/apgiii/apgiii.model';
import { APGIIIService } from 'app/entities/microservice/apgiii/service/apgiii.service';
import { IAPGIV } from 'app/entities/microservice/apgiv/apgiv.model';
import { APGIVService } from 'app/entities/microservice/apgiv/service/apgiv.service';

@Component({
  selector: 'perma-classification-update',
  templateUrl: './classification-update.component.html',
})
export class ClassificationUpdateComponent implements OnInit {
  isSaving = false;

  raunkiersCollection: IRaunkier[] = [];
  cronquistsCollection: ICronquist[] = [];
  apg1sCollection: IAPGI[] = [];
  apg2sCollection: IAPGII[] = [];
  apg3sCollection: IAPGIII[] = [];
  apg4sCollection: IAPGIV[] = [];

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
    protected raunkierService: RaunkierService,
    protected cronquistService: CronquistService,
    protected aPGIService: APGIService,
    protected aPGIIService: APGIIService,
    protected aPGIIIService: APGIIIService,
    protected aPGIVService: APGIVService,
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

  trackRaunkierById(index: number, item: IRaunkier): number {
    return item.id!;
  }

  trackCronquistById(index: number, item: ICronquist): number {
    return item.id!;
  }

  trackAPGIById(index: number, item: IAPGI): number {
    return item.id!;
  }

  trackAPGIIById(index: number, item: IAPGII): number {
    return item.id!;
  }

  trackAPGIIIById(index: number, item: IAPGIII): number {
    return item.id!;
  }

  trackAPGIVById(index: number, item: IAPGIV): number {
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

    this.raunkiersCollection = this.raunkierService.addRaunkierToCollectionIfMissing(this.raunkiersCollection, classification.raunkier);
    this.cronquistsCollection = this.cronquistService.addCronquistToCollectionIfMissing(
      this.cronquistsCollection,
      classification.cronquist
    );
    this.apg1sCollection = this.aPGIService.addAPGIToCollectionIfMissing(this.apg1sCollection, classification.apg1);
    this.apg2sCollection = this.aPGIIService.addAPGIIToCollectionIfMissing(this.apg2sCollection, classification.apg2);
    this.apg3sCollection = this.aPGIIIService.addAPGIIIToCollectionIfMissing(this.apg3sCollection, classification.apg3);
    this.apg4sCollection = this.aPGIVService.addAPGIVToCollectionIfMissing(this.apg4sCollection, classification.apg4);
  }

  protected loadRelationshipsOptions(): void {
    this.raunkierService
      .query({ filter: 'classification-is-null' })
      .pipe(map((res: HttpResponse<IRaunkier[]>) => res.body ?? []))
      .pipe(
        map((raunkiers: IRaunkier[]) =>
          this.raunkierService.addRaunkierToCollectionIfMissing(raunkiers, this.editForm.get('raunkier')!.value)
        )
      )
      .subscribe((raunkiers: IRaunkier[]) => (this.raunkiersCollection = raunkiers));

    this.cronquistService
      .query({ filter: 'classification-is-null' })
      .pipe(map((res: HttpResponse<ICronquist[]>) => res.body ?? []))
      .pipe(
        map((cronquists: ICronquist[]) =>
          this.cronquistService.addCronquistToCollectionIfMissing(cronquists, this.editForm.get('cronquist')!.value)
        )
      )
      .subscribe((cronquists: ICronquist[]) => (this.cronquistsCollection = cronquists));

    this.aPGIService
      .query({ filter: 'classification-is-null' })
      .pipe(map((res: HttpResponse<IAPGI[]>) => res.body ?? []))
      .pipe(map((aPGIS: IAPGI[]) => this.aPGIService.addAPGIToCollectionIfMissing(aPGIS, this.editForm.get('apg1')!.value)))
      .subscribe((aPGIS: IAPGI[]) => (this.apg1sCollection = aPGIS));

    this.aPGIIService
      .query({ filter: 'classification-is-null' })
      .pipe(map((res: HttpResponse<IAPGII[]>) => res.body ?? []))
      .pipe(map((aPGIIS: IAPGII[]) => this.aPGIIService.addAPGIIToCollectionIfMissing(aPGIIS, this.editForm.get('apg2')!.value)))
      .subscribe((aPGIIS: IAPGII[]) => (this.apg2sCollection = aPGIIS));

    this.aPGIIIService
      .query({ filter: 'classification-is-null' })
      .pipe(map((res: HttpResponse<IAPGIII[]>) => res.body ?? []))
      .pipe(map((aPGIIIS: IAPGIII[]) => this.aPGIIIService.addAPGIIIToCollectionIfMissing(aPGIIIS, this.editForm.get('apg3')!.value)))
      .subscribe((aPGIIIS: IAPGIII[]) => (this.apg3sCollection = aPGIIIS));

    this.aPGIVService
      .query({ filter: 'classification-is-null' })
      .pipe(map((res: HttpResponse<IAPGIV[]>) => res.body ?? []))
      .pipe(map((aPGIVS: IAPGIV[]) => this.aPGIVService.addAPGIVToCollectionIfMissing(aPGIVS, this.editForm.get('apg4')!.value)))
      .subscribe((aPGIVS: IAPGIV[]) => (this.apg4sCollection = aPGIVS));
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
