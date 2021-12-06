import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPlante, Plante } from '../plante.model';
import { PlanteService } from '../service/plante.service';
import { ICycleDeVie } from 'app/entities/cycle-de-vie/cycle-de-vie.model';
import { CycleDeVieService } from 'app/entities/cycle-de-vie/service/cycle-de-vie.service';
import { IClassification } from 'app/entities/classification/classification.model';
import { ClassificationService } from 'app/entities/classification/service/classification.service';
import { INomVernaculaire } from 'app/entities/nom-vernaculaire/nom-vernaculaire.model';
import { NomVernaculaireService } from 'app/entities/nom-vernaculaire/service/nom-vernaculaire.service';
import { ITemperature } from 'app/entities/temperature/temperature.model';
import { TemperatureService } from 'app/entities/temperature/service/temperature.service';
import { IRacine } from 'app/entities/racine/racine.model';
import { RacineService } from 'app/entities/racine/service/racine.service';
import { IStrate } from 'app/entities/strate/strate.model';
import { StrateService } from 'app/entities/strate/service/strate.service';
import { IFeuillage } from 'app/entities/feuillage/feuillage.model';
import { FeuillageService } from 'app/entities/feuillage/service/feuillage.service';

@Component({
  selector: 'jhi-plante-update',
  templateUrl: './plante-update.component.html',
})
export class PlanteUpdateComponent implements OnInit {
  isSaving = false;

  cycleDeViesCollection: ICycleDeVie[] = [];
  classificationsSharedCollection: IClassification[] = [];
  nomVernaculairesSharedCollection: INomVernaculaire[] = [];
  temperaturesSharedCollection: ITemperature[] = [];
  racinesSharedCollection: IRacine[] = [];
  stratesSharedCollection: IStrate[] = [];
  feuillagesSharedCollection: IFeuillage[] = [];

  editForm = this.fb.group({
    id: [],
    entretien: [],
    histoire: [],
    vitesseCroissance: [],
    exposition: [],
    cycleDeVie: [],
    classification: [],
    nomsVernaculaires: [],
    temperature: [],
    racine: [],
    strate: [],
    feuillage: [],
  });

  constructor(
    protected planteService: PlanteService,
    protected cycleDeVieService: CycleDeVieService,
    protected classificationService: ClassificationService,
    protected nomVernaculaireService: NomVernaculaireService,
    protected temperatureService: TemperatureService,
    protected racineService: RacineService,
    protected strateService: StrateService,
    protected feuillageService: FeuillageService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plante }) => {
      this.updateForm(plante);

      this.loadRelationshipsOptions();
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

  trackCycleDeVieById(index: number, item: ICycleDeVie): number {
    return item.id!;
  }

  trackClassificationById(index: number, item: IClassification): number {
    return item.id!;
  }

  trackNomVernaculaireById(index: number, item: INomVernaculaire): number {
    return item.id!;
  }

  trackTemperatureById(index: number, item: ITemperature): number {
    return item.id!;
  }

  trackRacineById(index: number, item: IRacine): number {
    return item.id!;
  }

  trackStrateById(index: number, item: IStrate): number {
    return item.id!;
  }

  trackFeuillageById(index: number, item: IFeuillage): number {
    return item.id!;
  }

  getSelectedNomVernaculaire(option: INomVernaculaire, selectedVals?: INomVernaculaire[]): INomVernaculaire {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlante>>): void {
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

  protected updateForm(plante: IPlante): void {
    this.editForm.patchValue({
      id: plante.id,
      entretien: plante.entretien,
      histoire: plante.histoire,
      vitesseCroissance: plante.vitesseCroissance,
      exposition: plante.exposition,
      cycleDeVie: plante.cycleDeVie,
      classification: plante.classification,
      nomsVernaculaires: plante.nomsVernaculaires,
      temperature: plante.temperature,
      racine: plante.racine,
      strate: plante.strate,
      feuillage: plante.feuillage,
    });

    this.cycleDeViesCollection = this.cycleDeVieService.addCycleDeVieToCollectionIfMissing(this.cycleDeViesCollection, plante.cycleDeVie);
    this.classificationsSharedCollection = this.classificationService.addClassificationToCollectionIfMissing(
      this.classificationsSharedCollection,
      plante.classification
    );
    this.nomVernaculairesSharedCollection = this.nomVernaculaireService.addNomVernaculaireToCollectionIfMissing(
      this.nomVernaculairesSharedCollection,
      ...(plante.nomsVernaculaires ?? [])
    );
    this.temperaturesSharedCollection = this.temperatureService.addTemperatureToCollectionIfMissing(
      this.temperaturesSharedCollection,
      plante.temperature
    );
    this.racinesSharedCollection = this.racineService.addRacineToCollectionIfMissing(this.racinesSharedCollection, plante.racine);
    this.stratesSharedCollection = this.strateService.addStrateToCollectionIfMissing(this.stratesSharedCollection, plante.strate);
    this.feuillagesSharedCollection = this.feuillageService.addFeuillageToCollectionIfMissing(
      this.feuillagesSharedCollection,
      plante.feuillage
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cycleDeVieService
      .query({ filter: 'plante-is-null' })
      .pipe(map((res: HttpResponse<ICycleDeVie[]>) => res.body ?? []))
      .pipe(
        map((cycleDeVies: ICycleDeVie[]) =>
          this.cycleDeVieService.addCycleDeVieToCollectionIfMissing(cycleDeVies, this.editForm.get('cycleDeVie')!.value)
        )
      )
      .subscribe((cycleDeVies: ICycleDeVie[]) => (this.cycleDeViesCollection = cycleDeVies));

    this.classificationService
      .query()
      .pipe(map((res: HttpResponse<IClassification[]>) => res.body ?? []))
      .pipe(
        map((classifications: IClassification[]) =>
          this.classificationService.addClassificationToCollectionIfMissing(classifications, this.editForm.get('classification')!.value)
        )
      )
      .subscribe((classifications: IClassification[]) => (this.classificationsSharedCollection = classifications));

    this.nomVernaculaireService
      .query()
      .pipe(map((res: HttpResponse<INomVernaculaire[]>) => res.body ?? []))
      .pipe(
        map((nomVernaculaires: INomVernaculaire[]) =>
          this.nomVernaculaireService.addNomVernaculaireToCollectionIfMissing(
            nomVernaculaires,
            ...(this.editForm.get('nomsVernaculaires')!.value ?? [])
          )
        )
      )
      .subscribe((nomVernaculaires: INomVernaculaire[]) => (this.nomVernaculairesSharedCollection = nomVernaculaires));

    this.temperatureService
      .query()
      .pipe(map((res: HttpResponse<ITemperature[]>) => res.body ?? []))
      .pipe(
        map((temperatures: ITemperature[]) =>
          this.temperatureService.addTemperatureToCollectionIfMissing(temperatures, this.editForm.get('temperature')!.value)
        )
      )
      .subscribe((temperatures: ITemperature[]) => (this.temperaturesSharedCollection = temperatures));

    this.racineService
      .query()
      .pipe(map((res: HttpResponse<IRacine[]>) => res.body ?? []))
      .pipe(map((racines: IRacine[]) => this.racineService.addRacineToCollectionIfMissing(racines, this.editForm.get('racine')!.value)))
      .subscribe((racines: IRacine[]) => (this.racinesSharedCollection = racines));

    this.strateService
      .query()
      .pipe(map((res: HttpResponse<IStrate[]>) => res.body ?? []))
      .pipe(map((strates: IStrate[]) => this.strateService.addStrateToCollectionIfMissing(strates, this.editForm.get('strate')!.value)))
      .subscribe((strates: IStrate[]) => (this.stratesSharedCollection = strates));

    this.feuillageService
      .query()
      .pipe(map((res: HttpResponse<IFeuillage[]>) => res.body ?? []))
      .pipe(
        map((feuillages: IFeuillage[]) =>
          this.feuillageService.addFeuillageToCollectionIfMissing(feuillages, this.editForm.get('feuillage')!.value)
        )
      )
      .subscribe((feuillages: IFeuillage[]) => (this.feuillagesSharedCollection = feuillages));
  }

  protected createFromForm(): IPlante {
    return {
      ...new Plante(),
      id: this.editForm.get(['id'])!.value,
      entretien: this.editForm.get(['entretien'])!.value,
      histoire: this.editForm.get(['histoire'])!.value,
      vitesseCroissance: this.editForm.get(['vitesseCroissance'])!.value,
      exposition: this.editForm.get(['exposition'])!.value,
      cycleDeVie: this.editForm.get(['cycleDeVie'])!.value,
      classification: this.editForm.get(['classification'])!.value,
      nomsVernaculaires: this.editForm.get(['nomsVernaculaires'])!.value,
      temperature: this.editForm.get(['temperature'])!.value,
      racine: this.editForm.get(['racine'])!.value,
      strate: this.editForm.get(['strate'])!.value,
      feuillage: this.editForm.get(['feuillage'])!.value,
    };
  }
}
