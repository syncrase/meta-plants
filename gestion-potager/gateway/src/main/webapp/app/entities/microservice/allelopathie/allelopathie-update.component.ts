import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IAllelopathie, Allelopathie } from 'app/shared/model/microservice/allelopathie.model';
import { AllelopathieService } from './allelopathie.service';
import { IPlante } from 'app/shared/model/microservice/plante.model';
import { PlanteService } from 'app/entities/microservice/plante/plante.service';

@Component({
  selector: 'gp-allelopathie-update',
  templateUrl: './allelopathie-update.component.html',
})
export class AllelopathieUpdateComponent implements OnInit {
  isSaving = false;
  cibles: IPlante[] = [];
  origines: IPlante[] = [];
  plantes: IPlante[] = [];

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required]],
    description: [],
    cibleId: [],
    origineId: [],
    planteId: [],
  });

  constructor(
    protected allelopathieService: AllelopathieService,
    protected planteService: PlanteService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ allelopathie }) => {
      this.updateForm(allelopathie);

      this.planteService
        .query({ 'allelopathieRecueId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IPlante[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPlante[]) => {
          if (!allelopathie.cibleId) {
            this.cibles = resBody;
          } else {
            this.planteService
              .find(allelopathie.cibleId)
              .pipe(
                map((subRes: HttpResponse<IPlante>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPlante[]) => (this.cibles = concatRes));
          }
        });

      this.planteService
        .query({ 'allelopathieProduiteId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IPlante[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IPlante[]) => {
          if (!allelopathie.origineId) {
            this.origines = resBody;
          } else {
            this.planteService
              .find(allelopathie.origineId)
              .pipe(
                map((subRes: HttpResponse<IPlante>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IPlante[]) => (this.origines = concatRes));
          }
        });

      this.planteService.query().subscribe((res: HttpResponse<IPlante[]>) => (this.plantes = res.body || []));
    });
  }

  updateForm(allelopathie: IAllelopathie): void {
    this.editForm.patchValue({
      id: allelopathie.id,
      type: allelopathie.type,
      description: allelopathie.description,
      cibleId: allelopathie.cibleId,
      origineId: allelopathie.origineId,
      planteId: allelopathie.planteId,
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

  private createFromForm(): IAllelopathie {
    return {
      ...new Allelopathie(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      description: this.editForm.get(['description'])!.value,
      cibleId: this.editForm.get(['cibleId'])!.value,
      origineId: this.editForm.get(['origineId'])!.value,
      planteId: this.editForm.get(['planteId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAllelopathie>>): void {
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

  trackById(index: number, item: IPlante): any {
    return item.id;
  }
}
