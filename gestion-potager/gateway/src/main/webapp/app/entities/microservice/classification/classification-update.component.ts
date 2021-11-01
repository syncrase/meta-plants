import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IClassification, Classification } from 'app/shared/model/microservice/classification.model';
import { ClassificationService } from './classification.service';
import { IRaunkier } from 'app/shared/model/microservice/raunkier.model';
import { RaunkierService } from 'app/entities/microservice/raunkier/raunkier.service';
import { ICronquist } from 'app/shared/model/microservice/cronquist.model';
import { CronquistService } from 'app/entities/microservice/cronquist/cronquist.service';
import { IAPGI } from 'app/shared/model/microservice/apgi.model';
import { APGIService } from 'app/entities/microservice/apgi/apgi.service';
import { IAPGII } from 'app/shared/model/microservice/apgii.model';
import { APGIIService } from 'app/entities/microservice/apgii/apgii.service';
import { IAPGIII } from 'app/shared/model/microservice/apgiii.model';
import { APGIIIService } from 'app/entities/microservice/apgiii/apgiii.service';
import { IAPGIV } from 'app/shared/model/microservice/apgiv.model';
import { APGIVService } from 'app/entities/microservice/apgiv/apgiv.service';

type SelectableEntity = IRaunkier | ICronquist | IAPGI | IAPGII | IAPGIII | IAPGIV;

@Component({
  selector: 'gp-classification-update',
  templateUrl: './classification-update.component.html',
})
export class ClassificationUpdateComponent implements OnInit {
  isSaving = false;
  raunkiers: IRaunkier[] = [];
  cronquists: ICronquist[] = [];
  apg1s: IAPGI[] = [];
  apg2s: IAPGII[] = [];
  apg3s: IAPGIII[] = [];
  apg4s: IAPGIV[] = [];

  editForm = this.fb.group({
    id: [],
    raunkierId: [],
    cronquistId: [],
    apg1Id: [],
    apg2Id: [],
    apg3Id: [],
    apg4Id: [],
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
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classification }) => {
      this.updateForm(classification);

      this.raunkierService
        .query({ 'classificationId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IRaunkier[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IRaunkier[]) => {
          if (!classification.raunkierId) {
            this.raunkiers = resBody;
          } else {
            this.raunkierService
              .find(classification.raunkierId)
              .pipe(
                map((subRes: HttpResponse<IRaunkier>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IRaunkier[]) => (this.raunkiers = concatRes));
          }
        });

      this.cronquistService
        .query({ 'classificationId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<ICronquist[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ICronquist[]) => {
          if (!classification.cronquistId) {
            this.cronquists = resBody;
          } else {
            this.cronquistService
              .find(classification.cronquistId)
              .pipe(
                map((subRes: HttpResponse<ICronquist>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ICronquist[]) => (this.cronquists = concatRes));
          }
        });

      this.aPGIService
        .query({ 'classificationId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IAPGI[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IAPGI[]) => {
          if (!classification.apg1Id) {
            this.apg1s = resBody;
          } else {
            this.aPGIService
              .find(classification.apg1Id)
              .pipe(
                map((subRes: HttpResponse<IAPGI>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IAPGI[]) => (this.apg1s = concatRes));
          }
        });

      this.aPGIIService
        .query({ 'classificationId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IAPGII[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IAPGII[]) => {
          if (!classification.apg2Id) {
            this.apg2s = resBody;
          } else {
            this.aPGIIService
              .find(classification.apg2Id)
              .pipe(
                map((subRes: HttpResponse<IAPGII>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IAPGII[]) => (this.apg2s = concatRes));
          }
        });

      this.aPGIIIService
        .query({ 'classificationId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IAPGIII[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IAPGIII[]) => {
          if (!classification.apg3Id) {
            this.apg3s = resBody;
          } else {
            this.aPGIIIService
              .find(classification.apg3Id)
              .pipe(
                map((subRes: HttpResponse<IAPGIII>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IAPGIII[]) => (this.apg3s = concatRes));
          }
        });

      this.aPGIVService
        .query({ 'classificationId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IAPGIV[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IAPGIV[]) => {
          if (!classification.apg4Id) {
            this.apg4s = resBody;
          } else {
            this.aPGIVService
              .find(classification.apg4Id)
              .pipe(
                map((subRes: HttpResponse<IAPGIV>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IAPGIV[]) => (this.apg4s = concatRes));
          }
        });
    });
  }

  updateForm(classification: IClassification): void {
    this.editForm.patchValue({
      id: classification.id,
      raunkierId: classification.raunkierId,
      cronquistId: classification.cronquistId,
      apg1Id: classification.apg1Id,
      apg2Id: classification.apg2Id,
      apg3Id: classification.apg3Id,
      apg4Id: classification.apg4Id,
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

  private createFromForm(): IClassification {
    return {
      ...new Classification(),
      id: this.editForm.get(['id'])!.value,
      raunkierId: this.editForm.get(['raunkierId'])!.value,
      cronquistId: this.editForm.get(['cronquistId'])!.value,
      apg1Id: this.editForm.get(['apg1Id'])!.value,
      apg2Id: this.editForm.get(['apg2Id'])!.value,
      apg3Id: this.editForm.get(['apg3Id'])!.value,
      apg4Id: this.editForm.get(['apg4Id'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassification>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
