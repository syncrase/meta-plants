import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ISemis, Semis } from 'app/shared/model/microservice/semis.model';
import { SemisService } from './semis.service';
import { IPeriodeAnnee } from 'app/shared/model/microservice/periode-annee.model';
import { PeriodeAnneeService } from 'app/entities/microservice/periode-annee/periode-annee.service';
import { ITypeSemis } from 'app/shared/model/microservice/type-semis.model';
import { TypeSemisService } from 'app/entities/microservice/type-semis/type-semis.service';
import { IGermination } from 'app/shared/model/microservice/germination.model';
import { GerminationService } from 'app/entities/microservice/germination/germination.service';

type SelectableEntity = IPeriodeAnnee | ITypeSemis | IGermination;

@Component({
  selector: 'gp-semis-update',
  templateUrl: './semis-update.component.html',
})
export class SemisUpdateComponent implements OnInit {
  isSaving = false;
  semispleineterres: IPeriodeAnnee[] = [];
  semissousabrises: IPeriodeAnnee[] = [];
  typesemis: ITypeSemis[] = [];
  germinations: IGermination[] = [];

  editForm = this.fb.group({
    id: [],
    semisPleineTerreId: [],
    semisSousAbrisId: [],
    typeSemisId: [],
    germinationId: [],
  });

  constructor(
    protected semisService: SemisService,
    protected periodeAnneeService: PeriodeAnneeService,
    protected typeSemisService: TypeSemisService,
    protected germinationService: GerminationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ semis }) => {
      this.updateForm(semis);

      this.periodeAnneeService
        .query({ 'semisId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IPeriodeAnnee[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPeriodeAnnee[]) => {
          if (!semis.semisPleineTerreId) {
            this.semispleineterres = resBody;
          } else {
            this.periodeAnneeService
              .find(semis.semisPleineTerreId)
              .pipe(
                map((subRes: HttpResponse<IPeriodeAnnee>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPeriodeAnnee[]) => (this.semispleineterres = concatRes));
          }
        });

      this.periodeAnneeService
        .query({ 'semisId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IPeriodeAnnee[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPeriodeAnnee[]) => {
          if (!semis.semisSousAbrisId) {
            this.semissousabrises = resBody;
          } else {
            this.periodeAnneeService
              .find(semis.semisSousAbrisId)
              .pipe(
                map((subRes: HttpResponse<IPeriodeAnnee>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPeriodeAnnee[]) => (this.semissousabrises = concatRes));
          }
        });

      this.typeSemisService
        .query({ 'semisId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<ITypeSemis[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ITypeSemis[]) => {
          if (!semis.typeSemisId) {
            this.typesemis = resBody;
          } else {
            this.typeSemisService
              .find(semis.typeSemisId)
              .pipe(
                map((subRes: HttpResponse<ITypeSemis>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ITypeSemis[]) => (this.typesemis = concatRes));
          }
        });

      this.germinationService
        .query({ 'semisId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IGermination[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IGermination[]) => {
          if (!semis.germinationId) {
            this.germinations = resBody;
          } else {
            this.germinationService
              .find(semis.germinationId)
              .pipe(
                map((subRes: HttpResponse<IGermination>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IGermination[]) => (this.germinations = concatRes));
          }
        });
    });
  }

  updateForm(semis: ISemis): void {
    this.editForm.patchValue({
      id: semis.id,
      semisPleineTerreId: semis.semisPleineTerreId,
      semisSousAbrisId: semis.semisSousAbrisId,
      typeSemisId: semis.typeSemisId,
      germinationId: semis.germinationId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const semis = this.createFromForm();
    if (semis.id !== undefined) {
      this.subscribeToSaveResponse(this.semisService.update(semis));
    } else {
      this.subscribeToSaveResponse(this.semisService.create(semis));
    }
  }

  private createFromForm(): ISemis {
    return {
      ...new Semis(),
      id: this.editForm.get(['id'])!.value,
      semisPleineTerreId: this.editForm.get(['semisPleineTerreId'])!.value,
      semisSousAbrisId: this.editForm.get(['semisSousAbrisId'])!.value,
      typeSemisId: this.editForm.get(['typeSemisId'])!.value,
      germinationId: this.editForm.get(['germinationId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISemis>>): void {
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
