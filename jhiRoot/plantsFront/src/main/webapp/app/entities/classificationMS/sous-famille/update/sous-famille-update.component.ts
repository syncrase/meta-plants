import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISousFamille, SousFamille } from '../sous-famille.model';
import { SousFamilleService } from '../service/sous-famille.service';
import { IFamille } from 'app/entities/classificationMS/famille/famille.model';
import { FamilleService } from 'app/entities/classificationMS/famille/service/famille.service';

@Component({
  selector: 'perma-sous-famille-update',
  templateUrl: './sous-famille-update.component.html',
})
export class SousFamilleUpdateComponent implements OnInit {
  isSaving = false;

  sousFamillesSharedCollection: ISousFamille[] = [];
  famillesSharedCollection: IFamille[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    famille: [],
    sousFamille: [],
  });

  constructor(
    protected sousFamilleService: SousFamilleService,
    protected familleService: FamilleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousFamille }) => {
      this.updateForm(sousFamille);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sousFamille = this.createFromForm();
    if (sousFamille.id !== undefined) {
      this.subscribeToSaveResponse(this.sousFamilleService.update(sousFamille));
    } else {
      this.subscribeToSaveResponse(this.sousFamilleService.create(sousFamille));
    }
  }

  trackSousFamilleById(index: number, item: ISousFamille): number {
    return item.id!;
  }

  trackFamilleById(index: number, item: IFamille): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISousFamille>>): void {
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

  protected updateForm(sousFamille: ISousFamille): void {
    this.editForm.patchValue({
      id: sousFamille.id,
      nomFr: sousFamille.nomFr,
      nomLatin: sousFamille.nomLatin,
      famille: sousFamille.famille,
      sousFamille: sousFamille.sousFamille,
    });

    this.sousFamillesSharedCollection = this.sousFamilleService.addSousFamilleToCollectionIfMissing(
      this.sousFamillesSharedCollection,
      sousFamille.sousFamille
    );
    this.famillesSharedCollection = this.familleService.addFamilleToCollectionIfMissing(this.famillesSharedCollection, sousFamille.famille);
  }

  protected loadRelationshipsOptions(): void {
    this.sousFamilleService
      .query()
      .pipe(map((res: HttpResponse<ISousFamille[]>) => res.body ?? []))
      .pipe(
        map((sousFamilles: ISousFamille[]) =>
          this.sousFamilleService.addSousFamilleToCollectionIfMissing(sousFamilles, this.editForm.get('sousFamille')!.value)
        )
      )
      .subscribe((sousFamilles: ISousFamille[]) => (this.sousFamillesSharedCollection = sousFamilles));

    this.familleService
      .query()
      .pipe(map((res: HttpResponse<IFamille[]>) => res.body ?? []))
      .pipe(
        map((familles: IFamille[]) => this.familleService.addFamilleToCollectionIfMissing(familles, this.editForm.get('famille')!.value))
      )
      .subscribe((familles: IFamille[]) => (this.famillesSharedCollection = familles));
  }

  protected createFromForm(): ISousFamille {
    return {
      ...new SousFamille(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      famille: this.editForm.get(['famille'])!.value,
      sousFamille: this.editForm.get(['sousFamille'])!.value,
    };
  }
}
