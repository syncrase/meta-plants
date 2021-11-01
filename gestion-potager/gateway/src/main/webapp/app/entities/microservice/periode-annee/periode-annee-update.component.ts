import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IPeriodeAnnee, PeriodeAnnee } from 'app/shared/model/microservice/periode-annee.model';
import { PeriodeAnneeService } from './periode-annee.service';
import { IMois } from 'app/shared/model/microservice/mois.model';
import { MoisService } from 'app/entities/microservice/mois/mois.service';

@Component({
  selector: 'gp-periode-annee-update',
  templateUrl: './periode-annee-update.component.html',
})
export class PeriodeAnneeUpdateComponent implements OnInit {
  isSaving = false;
  debuts: IMois[] = [];
  fins: IMois[] = [];

  editForm = this.fb.group({
    id: [],
    debutId: [null, Validators.required],
    finId: [null, Validators.required],
  });

  constructor(
    protected periodeAnneeService: PeriodeAnneeService,
    protected moisService: MoisService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ periodeAnnee }) => {
      this.updateForm(periodeAnnee);

      this.moisService
        .query({ 'periodeAnneeId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IMois[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IMois[]) => {
          if (!periodeAnnee.debutId) {
            this.debuts = resBody;
          } else {
            this.moisService
              .find(periodeAnnee.debutId)
              .pipe(
                map((subRes: HttpResponse<IMois>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IMois[]) => (this.debuts = concatRes));
          }
        });

      this.moisService
        .query({ 'periodeAnneeId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IMois[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IMois[]) => {
          if (!periodeAnnee.finId) {
            this.fins = resBody;
          } else {
            this.moisService
              .find(periodeAnnee.finId)
              .pipe(
                map((subRes: HttpResponse<IMois>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IMois[]) => (this.fins = concatRes));
          }
        });
    });
  }

  updateForm(periodeAnnee: IPeriodeAnnee): void {
    this.editForm.patchValue({
      id: periodeAnnee.id,
      debutId: periodeAnnee.debutId,
      finId: periodeAnnee.finId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const periodeAnnee = this.createFromForm();
    if (periodeAnnee.id !== undefined) {
      this.subscribeToSaveResponse(this.periodeAnneeService.update(periodeAnnee));
    } else {
      this.subscribeToSaveResponse(this.periodeAnneeService.create(periodeAnnee));
    }
  }

  private createFromForm(): IPeriodeAnnee {
    return {
      ...new PeriodeAnnee(),
      id: this.editForm.get(['id'])!.value,
      debutId: this.editForm.get(['debutId'])!.value,
      finId: this.editForm.get(['finId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPeriodeAnnee>>): void {
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

  trackById(index: number, item: IMois): any {
    return item.id;
  }
}
