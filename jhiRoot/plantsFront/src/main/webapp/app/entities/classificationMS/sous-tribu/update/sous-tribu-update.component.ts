import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISousTribu, SousTribu } from '../sous-tribu.model';
import { SousTribuService } from '../service/sous-tribu.service';
import { ITribu } from 'app/entities/classificationMS/tribu/tribu.model';
import { TribuService } from 'app/entities/classificationMS/tribu/service/tribu.service';

@Component({
  selector: 'perma-sous-tribu-update',
  templateUrl: './sous-tribu-update.component.html',
})
export class SousTribuUpdateComponent implements OnInit {
  isSaving = false;

  sousTribusSharedCollection: ISousTribu[] = [];
  tribusSharedCollection: ITribu[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    tribu: [],
    sousTribu: [],
  });

  constructor(
    protected sousTribuService: SousTribuService,
    protected tribuService: TribuService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousTribu }) => {
      this.updateForm(sousTribu);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sousTribu = this.createFromForm();
    if (sousTribu.id !== undefined) {
      this.subscribeToSaveResponse(this.sousTribuService.update(sousTribu));
    } else {
      this.subscribeToSaveResponse(this.sousTribuService.create(sousTribu));
    }
  }

  trackSousTribuById(index: number, item: ISousTribu): number {
    return item.id!;
  }

  trackTribuById(index: number, item: ITribu): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISousTribu>>): void {
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

  protected updateForm(sousTribu: ISousTribu): void {
    this.editForm.patchValue({
      id: sousTribu.id,
      nomFr: sousTribu.nomFr,
      nomLatin: sousTribu.nomLatin,
      tribu: sousTribu.tribu,
      sousTribu: sousTribu.sousTribu,
    });

    this.sousTribusSharedCollection = this.sousTribuService.addSousTribuToCollectionIfMissing(
      this.sousTribusSharedCollection,
      sousTribu.sousTribu
    );
    this.tribusSharedCollection = this.tribuService.addTribuToCollectionIfMissing(this.tribusSharedCollection, sousTribu.tribu);
  }

  protected loadRelationshipsOptions(): void {
    this.sousTribuService
      .query()
      .pipe(map((res: HttpResponse<ISousTribu[]>) => res.body ?? []))
      .pipe(
        map((sousTribus: ISousTribu[]) =>
          this.sousTribuService.addSousTribuToCollectionIfMissing(sousTribus, this.editForm.get('sousTribu')!.value)
        )
      )
      .subscribe((sousTribus: ISousTribu[]) => (this.sousTribusSharedCollection = sousTribus));

    this.tribuService
      .query()
      .pipe(map((res: HttpResponse<ITribu[]>) => res.body ?? []))
      .pipe(map((tribus: ITribu[]) => this.tribuService.addTribuToCollectionIfMissing(tribus, this.editForm.get('tribu')!.value)))
      .subscribe((tribus: ITribu[]) => (this.tribusSharedCollection = tribus));
  }

  protected createFromForm(): ISousTribu {
    return {
      ...new SousTribu(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      tribu: this.editForm.get(['tribu'])!.value,
      sousTribu: this.editForm.get(['sousTribu'])!.value,
    };
  }
}
