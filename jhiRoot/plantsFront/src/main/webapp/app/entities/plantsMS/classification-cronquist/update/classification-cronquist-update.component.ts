import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClassificationCronquist, ClassificationCronquist } from '../classification-cronquist.model';
import { ClassificationCronquistService } from '../service/classification-cronquist.service';
import { IPlante } from 'app/entities/plantsMS/plante/plante.model';
import { PlanteService } from 'app/entities/plantsMS/plante/service/plante.service';

@Component({
  selector: 'perma-classification-cronquist-update',
  templateUrl: './classification-cronquist-update.component.html',
})
export class ClassificationCronquistUpdateComponent implements OnInit {
  isSaving = false;

  plantesCollection: IPlante[] = [];

  editForm = this.fb.group({
    id: [],
    superRegne: [],
    regne: [],
    sousRegne: [],
    rameau: [],
    infraRegne: [],
    superEmbranchement: [],
    division: [],
    sousEmbranchement: [],
    infraEmbranchement: [],
    microEmbranchement: [],
    superClasse: [],
    classe: [],
    sousClasse: [],
    infraClasse: [],
    superOrdre: [],
    ordre: [],
    sousOrdre: [],
    infraOrdre: [],
    microOrdre: [],
    superFamille: [],
    famille: [],
    sousFamille: [],
    tribu: [],
    sousTribu: [],
    genre: [],
    sousGenre: [],
    section: [],
    sousSection: [],
    espece: [],
    sousEspece: [],
    variete: [],
    sousVariete: [],
    forme: [],
    plante: [],
  });

  constructor(
    protected classificationCronquistService: ClassificationCronquistService,
    protected planteService: PlanteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classificationCronquist }) => {
      this.updateForm(classificationCronquist);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classificationCronquist = this.createFromForm();
    if (classificationCronquist.id !== undefined) {
      this.subscribeToSaveResponse(this.classificationCronquistService.update(classificationCronquist));
    } else {
      this.subscribeToSaveResponse(this.classificationCronquistService.create(classificationCronquist));
    }
  }

  trackPlanteById(index: number, item: IPlante): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassificationCronquist>>): void {
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

  protected updateForm(classificationCronquist: IClassificationCronquist): void {
    this.editForm.patchValue({
      id: classificationCronquist.id,
      superRegne: classificationCronquist.superRegne,
      regne: classificationCronquist.regne,
      sousRegne: classificationCronquist.sousRegne,
      rameau: classificationCronquist.rameau,
      infraRegne: classificationCronquist.infraRegne,
      superEmbranchement: classificationCronquist.superEmbranchement,
      division: classificationCronquist.division,
      sousEmbranchement: classificationCronquist.sousEmbranchement,
      infraEmbranchement: classificationCronquist.infraEmbranchement,
      microEmbranchement: classificationCronquist.microEmbranchement,
      superClasse: classificationCronquist.superClasse,
      classe: classificationCronquist.classe,
      sousClasse: classificationCronquist.sousClasse,
      infraClasse: classificationCronquist.infraClasse,
      superOrdre: classificationCronquist.superOrdre,
      ordre: classificationCronquist.ordre,
      sousOrdre: classificationCronquist.sousOrdre,
      infraOrdre: classificationCronquist.infraOrdre,
      microOrdre: classificationCronquist.microOrdre,
      superFamille: classificationCronquist.superFamille,
      famille: classificationCronquist.famille,
      sousFamille: classificationCronquist.sousFamille,
      tribu: classificationCronquist.tribu,
      sousTribu: classificationCronquist.sousTribu,
      genre: classificationCronquist.genre,
      sousGenre: classificationCronquist.sousGenre,
      section: classificationCronquist.section,
      sousSection: classificationCronquist.sousSection,
      espece: classificationCronquist.espece,
      sousEspece: classificationCronquist.sousEspece,
      variete: classificationCronquist.variete,
      sousVariete: classificationCronquist.sousVariete,
      forme: classificationCronquist.forme,
      plante: classificationCronquist.plante,
    });

    this.plantesCollection = this.planteService.addPlanteToCollectionIfMissing(this.plantesCollection, classificationCronquist.plante);
  }

  protected loadRelationshipsOptions(): void {
    this.planteService
      .query({ filter: 'classificationcronquist-is-null' })
      .pipe(map((res: HttpResponse<IPlante[]>) => res.body ?? []))
      .pipe(map((plantes: IPlante[]) => this.planteService.addPlanteToCollectionIfMissing(plantes, this.editForm.get('plante')!.value)))
      .subscribe((plantes: IPlante[]) => (this.plantesCollection = plantes));
  }

  protected createFromForm(): IClassificationCronquist {
    return {
      ...new ClassificationCronquist(),
      id: this.editForm.get(['id'])!.value,
      superRegne: this.editForm.get(['superRegne'])!.value,
      regne: this.editForm.get(['regne'])!.value,
      sousRegne: this.editForm.get(['sousRegne'])!.value,
      rameau: this.editForm.get(['rameau'])!.value,
      infraRegne: this.editForm.get(['infraRegne'])!.value,
      superEmbranchement: this.editForm.get(['superEmbranchement'])!.value,
      division: this.editForm.get(['division'])!.value,
      sousEmbranchement: this.editForm.get(['sousEmbranchement'])!.value,
      infraEmbranchement: this.editForm.get(['infraEmbranchement'])!.value,
      microEmbranchement: this.editForm.get(['microEmbranchement'])!.value,
      superClasse: this.editForm.get(['superClasse'])!.value,
      classe: this.editForm.get(['classe'])!.value,
      sousClasse: this.editForm.get(['sousClasse'])!.value,
      infraClasse: this.editForm.get(['infraClasse'])!.value,
      superOrdre: this.editForm.get(['superOrdre'])!.value,
      ordre: this.editForm.get(['ordre'])!.value,
      sousOrdre: this.editForm.get(['sousOrdre'])!.value,
      infraOrdre: this.editForm.get(['infraOrdre'])!.value,
      microOrdre: this.editForm.get(['microOrdre'])!.value,
      superFamille: this.editForm.get(['superFamille'])!.value,
      famille: this.editForm.get(['famille'])!.value,
      sousFamille: this.editForm.get(['sousFamille'])!.value,
      tribu: this.editForm.get(['tribu'])!.value,
      sousTribu: this.editForm.get(['sousTribu'])!.value,
      genre: this.editForm.get(['genre'])!.value,
      sousGenre: this.editForm.get(['sousGenre'])!.value,
      section: this.editForm.get(['section'])!.value,
      sousSection: this.editForm.get(['sousSection'])!.value,
      espece: this.editForm.get(['espece'])!.value,
      sousEspece: this.editForm.get(['sousEspece'])!.value,
      variete: this.editForm.get(['variete'])!.value,
      sousVariete: this.editForm.get(['sousVariete'])!.value,
      forme: this.editForm.get(['forme'])!.value,
      plante: this.editForm.get(['plante'])!.value,
    };
  }
}
