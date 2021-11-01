import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ICycleDeVie, CycleDeVie } from 'app/shared/model/microservice/cycle-de-vie.model';
import { CycleDeVieService } from './cycle-de-vie.service';
import { ISemis } from 'app/shared/model/microservice/semis.model';
import { SemisService } from 'app/entities/microservice/semis/semis.service';
import { IPeriodeAnnee } from 'app/shared/model/microservice/periode-annee.model';
import { PeriodeAnneeService } from 'app/entities/microservice/periode-annee/periode-annee.service';

type SelectableEntity = ISemis | IPeriodeAnnee;

@Component({
  selector: 'gp-cycle-de-vie-update',
  templateUrl: './cycle-de-vie-update.component.html',
})
export class CycleDeVieUpdateComponent implements OnInit {
  isSaving = false;
  semis: ISemis[] = [];
  apparitionfeuilles: IPeriodeAnnee[] = [];
  floraisons: IPeriodeAnnee[] = [];
  recoltes: IPeriodeAnnee[] = [];
  croissances: IPeriodeAnnee[] = [];
  maturites: IPeriodeAnnee[] = [];
  plantations: IPeriodeAnnee[] = [];
  rempotages: IPeriodeAnnee[] = [];

  editForm = this.fb.group({
    id: [],
    vitesseDeCroissance: [],
    semisId: [],
    apparitionFeuillesId: [],
    floraisonId: [],
    recolteId: [],
    croissanceId: [],
    maturiteId: [],
    plantationId: [],
    rempotageId: [],
  });

  constructor(
    protected cycleDeVieService: CycleDeVieService,
    protected semisService: SemisService,
    protected periodeAnneeService: PeriodeAnneeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cycleDeVie }) => {
      this.updateForm(cycleDeVie);

      this.semisService
        .query({ 'cycleDeVieId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<ISemis[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ISemis[]) => {
          if (!cycleDeVie.semisId) {
            this.semis = resBody;
          } else {
            this.semisService
              .find(cycleDeVie.semisId)
              .pipe(
                map((subRes: HttpResponse<ISemis>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ISemis[]) => (this.semis = concatRes));
          }
        });

      this.periodeAnneeService
        .query({ 'cycleDeVieId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IPeriodeAnnee[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPeriodeAnnee[]) => {
          if (!cycleDeVie.apparitionFeuillesId) {
            this.apparitionfeuilles = resBody;
          } else {
            this.periodeAnneeService
              .find(cycleDeVie.apparitionFeuillesId)
              .pipe(
                map((subRes: HttpResponse<IPeriodeAnnee>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPeriodeAnnee[]) => (this.apparitionfeuilles = concatRes));
          }
        });

      this.periodeAnneeService
        .query({ 'cycleDeVieId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IPeriodeAnnee[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPeriodeAnnee[]) => {
          if (!cycleDeVie.floraisonId) {
            this.floraisons = resBody;
          } else {
            this.periodeAnneeService
              .find(cycleDeVie.floraisonId)
              .pipe(
                map((subRes: HttpResponse<IPeriodeAnnee>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPeriodeAnnee[]) => (this.floraisons = concatRes));
          }
        });

      this.periodeAnneeService
        .query({ 'cycleDeVieId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IPeriodeAnnee[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPeriodeAnnee[]) => {
          if (!cycleDeVie.recolteId) {
            this.recoltes = resBody;
          } else {
            this.periodeAnneeService
              .find(cycleDeVie.recolteId)
              .pipe(
                map((subRes: HttpResponse<IPeriodeAnnee>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPeriodeAnnee[]) => (this.recoltes = concatRes));
          }
        });

      this.periodeAnneeService
        .query({ 'cycleDeVieId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IPeriodeAnnee[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPeriodeAnnee[]) => {
          if (!cycleDeVie.croissanceId) {
            this.croissances = resBody;
          } else {
            this.periodeAnneeService
              .find(cycleDeVie.croissanceId)
              .pipe(
                map((subRes: HttpResponse<IPeriodeAnnee>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPeriodeAnnee[]) => (this.croissances = concatRes));
          }
        });

      this.periodeAnneeService
        .query({ 'cycleDeVieId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IPeriodeAnnee[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPeriodeAnnee[]) => {
          if (!cycleDeVie.maturiteId) {
            this.maturites = resBody;
          } else {
            this.periodeAnneeService
              .find(cycleDeVie.maturiteId)
              .pipe(
                map((subRes: HttpResponse<IPeriodeAnnee>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPeriodeAnnee[]) => (this.maturites = concatRes));
          }
        });

      this.periodeAnneeService
        .query({ 'cycleDeVieId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IPeriodeAnnee[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPeriodeAnnee[]) => {
          if (!cycleDeVie.plantationId) {
            this.plantations = resBody;
          } else {
            this.periodeAnneeService
              .find(cycleDeVie.plantationId)
              .pipe(
                map((subRes: HttpResponse<IPeriodeAnnee>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPeriodeAnnee[]) => (this.plantations = concatRes));
          }
        });

      this.periodeAnneeService
        .query({ 'cycleDeVieId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IPeriodeAnnee[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPeriodeAnnee[]) => {
          if (!cycleDeVie.rempotageId) {
            this.rempotages = resBody;
          } else {
            this.periodeAnneeService
              .find(cycleDeVie.rempotageId)
              .pipe(
                map((subRes: HttpResponse<IPeriodeAnnee>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPeriodeAnnee[]) => (this.rempotages = concatRes));
          }
        });
    });
  }

  updateForm(cycleDeVie: ICycleDeVie): void {
    this.editForm.patchValue({
      id: cycleDeVie.id,
      vitesseDeCroissance: cycleDeVie.vitesseDeCroissance,
      semisId: cycleDeVie.semisId,
      apparitionFeuillesId: cycleDeVie.apparitionFeuillesId,
      floraisonId: cycleDeVie.floraisonId,
      recolteId: cycleDeVie.recolteId,
      croissanceId: cycleDeVie.croissanceId,
      maturiteId: cycleDeVie.maturiteId,
      plantationId: cycleDeVie.plantationId,
      rempotageId: cycleDeVie.rempotageId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cycleDeVie = this.createFromForm();
    if (cycleDeVie.id !== undefined) {
      this.subscribeToSaveResponse(this.cycleDeVieService.update(cycleDeVie));
    } else {
      this.subscribeToSaveResponse(this.cycleDeVieService.create(cycleDeVie));
    }
  }

  private createFromForm(): ICycleDeVie {
    return {
      ...new CycleDeVie(),
      id: this.editForm.get(['id'])!.value,
      vitesseDeCroissance: this.editForm.get(['vitesseDeCroissance'])!.value,
      semisId: this.editForm.get(['semisId'])!.value,
      apparitionFeuillesId: this.editForm.get(['apparitionFeuillesId'])!.value,
      floraisonId: this.editForm.get(['floraisonId'])!.value,
      recolteId: this.editForm.get(['recolteId'])!.value,
      croissanceId: this.editForm.get(['croissanceId'])!.value,
      maturiteId: this.editForm.get(['maturiteId'])!.value,
      plantationId: this.editForm.get(['plantationId'])!.value,
      rempotageId: this.editForm.get(['rempotageId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICycleDeVie>>): void {
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
