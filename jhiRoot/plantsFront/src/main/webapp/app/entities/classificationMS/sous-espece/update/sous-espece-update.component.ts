import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISousEspece, SousEspece } from '../sous-espece.model';
import { SousEspeceService } from '../service/sous-espece.service';
import { IEspece } from 'app/entities/classificationMS/espece/espece.model';
import { EspeceService } from 'app/entities/classificationMS/espece/service/espece.service';

@Component({
  selector: 'perma-sous-espece-update',
  templateUrl: './sous-espece-update.component.html',
})
export class SousEspeceUpdateComponent implements OnInit {
  isSaving = false;

  sousEspecesSharedCollection: ISousEspece[] = [];
  especesSharedCollection: IEspece[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    espece: [],
    sousEspece: [],
  });

  constructor(
    protected sousEspeceService: SousEspeceService,
    protected especeService: EspeceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousEspece }) => {
      this.updateForm(sousEspece);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sousEspece = this.createFromForm();
    if (sousEspece.id !== undefined) {
      this.subscribeToSaveResponse(this.sousEspeceService.update(sousEspece));
    } else {
      this.subscribeToSaveResponse(this.sousEspeceService.create(sousEspece));
    }
  }

  trackSousEspeceById(index: number, item: ISousEspece): number {
    return item.id!;
  }

  trackEspeceById(index: number, item: IEspece): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISousEspece>>): void {
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

  protected updateForm(sousEspece: ISousEspece): void {
    this.editForm.patchValue({
      id: sousEspece.id,
      nomFr: sousEspece.nomFr,
      nomLatin: sousEspece.nomLatin,
      espece: sousEspece.espece,
      sousEspece: sousEspece.sousEspece,
    });

    this.sousEspecesSharedCollection = this.sousEspeceService.addSousEspeceToCollectionIfMissing(
      this.sousEspecesSharedCollection,
      sousEspece.sousEspece
    );
    this.especesSharedCollection = this.especeService.addEspeceToCollectionIfMissing(this.especesSharedCollection, sousEspece.espece);
  }

  protected loadRelationshipsOptions(): void {
    this.sousEspeceService
      .query()
      .pipe(map((res: HttpResponse<ISousEspece[]>) => res.body ?? []))
      .pipe(
        map((sousEspeces: ISousEspece[]) =>
          this.sousEspeceService.addSousEspeceToCollectionIfMissing(sousEspeces, this.editForm.get('sousEspece')!.value)
        )
      )
      .subscribe((sousEspeces: ISousEspece[]) => (this.sousEspecesSharedCollection = sousEspeces));

    this.especeService
      .query()
      .pipe(map((res: HttpResponse<IEspece[]>) => res.body ?? []))
      .pipe(map((especes: IEspece[]) => this.especeService.addEspeceToCollectionIfMissing(especes, this.editForm.get('espece')!.value)))
      .subscribe((especes: IEspece[]) => (this.especesSharedCollection = especes));
  }

  protected createFromForm(): ISousEspece {
    return {
      ...new SousEspece(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      espece: this.editForm.get(['espece'])!.value,
      sousEspece: this.editForm.get(['sousEspece'])!.value,
    };
  }
}
