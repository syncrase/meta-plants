import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFamille, Famille } from '../famille.model';
import { FamilleService } from '../service/famille.service';
import { ISuperFamille } from 'app/entities/classificationMS/super-famille/super-famille.model';
import { SuperFamilleService } from 'app/entities/classificationMS/super-famille/service/super-famille.service';

@Component({
  selector: 'perma-famille-update',
  templateUrl: './famille-update.component.html',
})
export class FamilleUpdateComponent implements OnInit {
  isSaving = false;

  famillesSharedCollection: IFamille[] = [];
  superFamillesSharedCollection: ISuperFamille[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    superFamille: [],
    famille: [],
  });

  constructor(
    protected familleService: FamilleService,
    protected superFamilleService: SuperFamilleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ famille }) => {
      this.updateForm(famille);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const famille = this.createFromForm();
    if (famille.id !== undefined) {
      this.subscribeToSaveResponse(this.familleService.update(famille));
    } else {
      this.subscribeToSaveResponse(this.familleService.create(famille));
    }
  }

  trackFamilleById(index: number, item: IFamille): number {
    return item.id!;
  }

  trackSuperFamilleById(index: number, item: ISuperFamille): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFamille>>): void {
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

  protected updateForm(famille: IFamille): void {
    this.editForm.patchValue({
      id: famille.id,
      nomFr: famille.nomFr,
      nomLatin: famille.nomLatin,
      superFamille: famille.superFamille,
      famille: famille.famille,
    });

    this.famillesSharedCollection = this.familleService.addFamilleToCollectionIfMissing(this.famillesSharedCollection, famille.famille);
    this.superFamillesSharedCollection = this.superFamilleService.addSuperFamilleToCollectionIfMissing(
      this.superFamillesSharedCollection,
      famille.superFamille
    );
  }

  protected loadRelationshipsOptions(): void {
    this.familleService
      .query()
      .pipe(map((res: HttpResponse<IFamille[]>) => res.body ?? []))
      .pipe(
        map((familles: IFamille[]) => this.familleService.addFamilleToCollectionIfMissing(familles, this.editForm.get('famille')!.value))
      )
      .subscribe((familles: IFamille[]) => (this.famillesSharedCollection = familles));

    this.superFamilleService
      .query()
      .pipe(map((res: HttpResponse<ISuperFamille[]>) => res.body ?? []))
      .pipe(
        map((superFamilles: ISuperFamille[]) =>
          this.superFamilleService.addSuperFamilleToCollectionIfMissing(superFamilles, this.editForm.get('superFamille')!.value)
        )
      )
      .subscribe((superFamilles: ISuperFamille[]) => (this.superFamillesSharedCollection = superFamilles));
  }

  protected createFromForm(): IFamille {
    return {
      ...new Famille(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      superFamille: this.editForm.get(['superFamille'])!.value,
      famille: this.editForm.get(['famille'])!.value,
    };
  }
}
