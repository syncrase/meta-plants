import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { IPlante, Plante } from 'app/shared/model/microservice/plante.model';
import { PlanteService } from './plante.service';
import { ICycleDeVie } from 'app/shared/model/microservice/cycle-de-vie.model';
import { CycleDeVieService } from 'app/entities/microservice/cycle-de-vie/cycle-de-vie.service';
import { IClassification } from 'app/shared/model/microservice/classification.model';
import { ClassificationService } from 'app/entities/microservice/classification/classification.service';
import { INomVernaculaire } from 'app/shared/model/microservice/nom-vernaculaire.model';
import { NomVernaculaireService } from 'app/entities/microservice/nom-vernaculaire/nom-vernaculaire.service';

type SelectableEntity = ICycleDeVie | IClassification | INomVernaculaire;

@Component({
  selector: 'gp-plante-update',
  templateUrl: './plante-update.component.html',
})
export class PlanteUpdateComponent implements OnInit {
  isSaving = false;
  cycledevies: ICycleDeVie[] = [];
  classifications: IClassification[] = [];
  nomvernaculaires: INomVernaculaire[] = [];

  editForm = this.fb.group({
    id: [],
    nomLatin: [null, [Validators.required]],
    entretien: [],
    histoire: [],
    exposition: [],
    rusticite: [],
    cycleDeVieId: [],
    classificationId: [],
    nomsVernaculaires: [],
  });

  constructor(
    protected planteService: PlanteService,
    protected cycleDeVieService: CycleDeVieService,
    protected classificationService: ClassificationService,
    protected nomVernaculaireService: NomVernaculaireService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plante }) => {
      this.updateForm(plante);

      this.cycleDeVieService
        .query({ 'planteId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<ICycleDeVie[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: ICycleDeVie[]) => {
          if (!plante.cycleDeVieId) {
            this.cycledevies = resBody;
          } else {
            this.cycleDeVieService
              .find(plante.cycleDeVieId)
              .pipe(
                map((subRes: HttpResponse<ICycleDeVie>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: ICycleDeVie[]) => (this.cycledevies = concatRes));
          }
        });

      this.classificationService
        .query({ 'planteId.specified': 'false' })
        .pipe(
          map((res: HttpResponse<IClassification[]>) => {
            return res.body || [];
          })
        )
        .subscribe((resBody: IClassification[]) => {
          if (!plante.classificationId) {
            this.classifications = resBody;
          } else {
            this.classificationService
              .find(plante.classificationId)
              .pipe(
                map((subRes: HttpResponse<IClassification>) => {
                  return subRes.body ? [subRes.body].concat(resBody) : resBody;
                })
              )
              .subscribe((concatRes: IClassification[]) => (this.classifications = concatRes));
          }
        });

      this.nomVernaculaireService.query().subscribe((res: HttpResponse<INomVernaculaire[]>) => (this.nomvernaculaires = res.body || []));
    });
  }

  updateForm(plante: IPlante): void {
    this.editForm.patchValue({
      id: plante.id,
      nomLatin: plante.nomLatin,
      entretien: plante.entretien,
      histoire: plante.histoire,
      exposition: plante.exposition,
      rusticite: plante.rusticite,
      cycleDeVieId: plante.cycleDeVieId,
      classificationId: plante.classificationId,
      nomsVernaculaires: plante.nomsVernaculaires,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const plante = this.createFromForm();
    if (plante.id !== undefined) {
      this.subscribeToSaveResponse(this.planteService.update(plante));
    } else {
      this.subscribeToSaveResponse(this.planteService.create(plante));
    }
  }

  private createFromForm(): IPlante {
    return {
      ...new Plante(),
      id: this.editForm.get(['id'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      entretien: this.editForm.get(['entretien'])!.value,
      histoire: this.editForm.get(['histoire'])!.value,
      exposition: this.editForm.get(['exposition'])!.value,
      rusticite: this.editForm.get(['rusticite'])!.value,
      cycleDeVieId: this.editForm.get(['cycleDeVieId'])!.value,
      classificationId: this.editForm.get(['classificationId'])!.value,
      nomsVernaculaires: this.editForm.get(['nomsVernaculaires'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlante>>): void {
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

  getSelected(selectedVals: INomVernaculaire[], option: INomVernaculaire): INomVernaculaire {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
