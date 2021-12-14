import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAPGIIIPlante, APGIIIPlante } from '../apgiii-plante.model';
import { APGIIIPlanteService } from '../service/apgiii-plante.service';
import { IClade } from 'app/entities/plantsMS/clade/clade.model';
import { CladeService } from 'app/entities/plantsMS/clade/service/clade.service';

@Component({
  selector: 'perma-apgiii-plante-update',
  templateUrl: './apgiii-plante-update.component.html',
})
export class APGIIIPlanteUpdateComponent implements OnInit {
  isSaving = false;

  cladesSharedCollection: IClade[] = [];

  editForm = this.fb.group({
    id: [],
    ordre: [null, [Validators.required]],
    famille: [null, [Validators.required]],
    sousFamille: [null, [Validators.required]],
    tribu: [],
    sousTribu: [],
    genre: [null, [Validators.required]],
    clades: [],
  });

  constructor(
    protected aPGIIIPlanteService: APGIIIPlanteService,
    protected cladeService: CladeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGIIIPlante }) => {
      this.updateForm(aPGIIIPlante);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aPGIIIPlante = this.createFromForm();
    if (aPGIIIPlante.id !== undefined) {
      this.subscribeToSaveResponse(this.aPGIIIPlanteService.update(aPGIIIPlante));
    } else {
      this.subscribeToSaveResponse(this.aPGIIIPlanteService.create(aPGIIIPlante));
    }
  }

  trackCladeById(index: number, item: IClade): number {
    return item.id!;
  }

  getSelectedClade(option: IClade, selectedVals?: IClade[]): IClade {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAPGIIIPlante>>): void {
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

  protected updateForm(aPGIIIPlante: IAPGIIIPlante): void {
    this.editForm.patchValue({
      id: aPGIIIPlante.id,
      ordre: aPGIIIPlante.ordre,
      famille: aPGIIIPlante.famille,
      sousFamille: aPGIIIPlante.sousFamille,
      tribu: aPGIIIPlante.tribu,
      sousTribu: aPGIIIPlante.sousTribu,
      genre: aPGIIIPlante.genre,
      clades: aPGIIIPlante.clades,
    });

    this.cladesSharedCollection = this.cladeService.addCladeToCollectionIfMissing(
      this.cladesSharedCollection,
      ...(aPGIIIPlante.clades ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cladeService
      .query()
      .pipe(map((res: HttpResponse<IClade[]>) => res.body ?? []))
      .pipe(
        map((clades: IClade[]) => this.cladeService.addCladeToCollectionIfMissing(clades, ...(this.editForm.get('clades')!.value ?? [])))
      )
      .subscribe((clades: IClade[]) => (this.cladesSharedCollection = clades));
  }

  protected createFromForm(): IAPGIIIPlante {
    return {
      ...new APGIIIPlante(),
      id: this.editForm.get(['id'])!.value,
      ordre: this.editForm.get(['ordre'])!.value,
      famille: this.editForm.get(['famille'])!.value,
      sousFamille: this.editForm.get(['sousFamille'])!.value,
      tribu: this.editForm.get(['tribu'])!.value,
      sousTribu: this.editForm.get(['sousTribu'])!.value,
      genre: this.editForm.get(['genre'])!.value,
      clades: this.editForm.get(['clades'])!.value,
    };
  }
}
