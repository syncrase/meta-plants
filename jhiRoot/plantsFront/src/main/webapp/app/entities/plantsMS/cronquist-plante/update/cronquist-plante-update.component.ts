import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICronquistPlante, CronquistPlante } from '../cronquist-plante.model';
import { CronquistPlanteService } from '../service/cronquist-plante.service';

@Component({
  selector: 'perma-cronquist-plante-update',
  templateUrl: './cronquist-plante-update.component.html',
})
export class CronquistPlanteUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    superRegne: [],
    regne: [],
    sousRegne: [],
    rameau: [],
    infraRegne: [],
    superDivision: [],
    division: [],
    sousDivision: [],
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
  });

  constructor(
    protected cronquistPlanteService: CronquistPlanteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cronquistPlante }) => {
      this.updateForm(cronquistPlante);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cronquistPlante = this.createFromForm();
    if (cronquistPlante.id !== undefined) {
      this.subscribeToSaveResponse(this.cronquistPlanteService.update(cronquistPlante));
    } else {
      this.subscribeToSaveResponse(this.cronquistPlanteService.create(cronquistPlante));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICronquistPlante>>): void {
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

  protected updateForm(cronquistPlante: ICronquistPlante): void {
    this.editForm.patchValue({
      id: cronquistPlante.id,
      superRegne: cronquistPlante.superRegne,
      regne: cronquistPlante.regne,
      sousRegne: cronquistPlante.sousRegne,
      rameau: cronquistPlante.rameau,
      infraRegne: cronquistPlante.infraRegne,
      superDivision: cronquistPlante.superDivision,
      division: cronquistPlante.division,
      sousDivision: cronquistPlante.sousDivision,
      infraEmbranchement: cronquistPlante.infraEmbranchement,
      microEmbranchement: cronquistPlante.microEmbranchement,
      superClasse: cronquistPlante.superClasse,
      classe: cronquistPlante.classe,
      sousClasse: cronquistPlante.sousClasse,
      infraClasse: cronquistPlante.infraClasse,
      superOrdre: cronquistPlante.superOrdre,
      ordre: cronquistPlante.ordre,
      sousOrdre: cronquistPlante.sousOrdre,
      infraOrdre: cronquistPlante.infraOrdre,
      microOrdre: cronquistPlante.microOrdre,
      superFamille: cronquistPlante.superFamille,
      famille: cronquistPlante.famille,
      sousFamille: cronquistPlante.sousFamille,
      tribu: cronquistPlante.tribu,
      sousTribu: cronquistPlante.sousTribu,
      genre: cronquistPlante.genre,
      sousGenre: cronquistPlante.sousGenre,
      section: cronquistPlante.section,
      sousSection: cronquistPlante.sousSection,
      espece: cronquistPlante.espece,
      sousEspece: cronquistPlante.sousEspece,
      variete: cronquistPlante.variete,
      sousVariete: cronquistPlante.sousVariete,
      forme: cronquistPlante.forme,
    });
  }

  protected createFromForm(): ICronquistPlante {
    return {
      ...new CronquistPlante(),
      id: this.editForm.get(['id'])!.value,
      superRegne: this.editForm.get(['superRegne'])!.value,
      regne: this.editForm.get(['regne'])!.value,
      sousRegne: this.editForm.get(['sousRegne'])!.value,
      rameau: this.editForm.get(['rameau'])!.value,
      infraRegne: this.editForm.get(['infraRegne'])!.value,
      superDivision: this.editForm.get(['superDivision'])!.value,
      division: this.editForm.get(['division'])!.value,
      sousDivision: this.editForm.get(['sousDivision'])!.value,
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
    };
  }
}
