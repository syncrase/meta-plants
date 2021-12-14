import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITribu, Tribu } from '../tribu.model';
import { TribuService } from '../service/tribu.service';
import { ISousFamille } from 'app/entities/classificationMS/sous-famille/sous-famille.model';
import { SousFamilleService } from 'app/entities/classificationMS/sous-famille/service/sous-famille.service';

@Component({
  selector: 'perma-tribu-update',
  templateUrl: './tribu-update.component.html',
})
export class TribuUpdateComponent implements OnInit {
  isSaving = false;

  tribusSharedCollection: ITribu[] = [];
  sousFamillesSharedCollection: ISousFamille[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    sousFamille: [],
    tribu: [],
  });

  constructor(
    protected tribuService: TribuService,
    protected sousFamilleService: SousFamilleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tribu }) => {
      this.updateForm(tribu);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tribu = this.createFromForm();
    if (tribu.id !== undefined) {
      this.subscribeToSaveResponse(this.tribuService.update(tribu));
    } else {
      this.subscribeToSaveResponse(this.tribuService.create(tribu));
    }
  }

  trackTribuById(index: number, item: ITribu): number {
    return item.id!;
  }

  trackSousFamilleById(index: number, item: ISousFamille): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITribu>>): void {
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

  protected updateForm(tribu: ITribu): void {
    this.editForm.patchValue({
      id: tribu.id,
      nomFr: tribu.nomFr,
      nomLatin: tribu.nomLatin,
      sousFamille: tribu.sousFamille,
      tribu: tribu.tribu,
    });

    this.tribusSharedCollection = this.tribuService.addTribuToCollectionIfMissing(this.tribusSharedCollection, tribu.tribu);
    this.sousFamillesSharedCollection = this.sousFamilleService.addSousFamilleToCollectionIfMissing(
      this.sousFamillesSharedCollection,
      tribu.sousFamille
    );
  }

  protected loadRelationshipsOptions(): void {
    this.tribuService
      .query()
      .pipe(map((res: HttpResponse<ITribu[]>) => res.body ?? []))
      .pipe(map((tribus: ITribu[]) => this.tribuService.addTribuToCollectionIfMissing(tribus, this.editForm.get('tribu')!.value)))
      .subscribe((tribus: ITribu[]) => (this.tribusSharedCollection = tribus));

    this.sousFamilleService
      .query()
      .pipe(map((res: HttpResponse<ISousFamille[]>) => res.body ?? []))
      .pipe(
        map((sousFamilles: ISousFamille[]) =>
          this.sousFamilleService.addSousFamilleToCollectionIfMissing(sousFamilles, this.editForm.get('sousFamille')!.value)
        )
      )
      .subscribe((sousFamilles: ISousFamille[]) => (this.sousFamillesSharedCollection = sousFamilles));
  }

  protected createFromForm(): ITribu {
    return {
      ...new Tribu(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      sousFamille: this.editForm.get(['sousFamille'])!.value,
      tribu: this.editForm.get(['tribu'])!.value,
    };
  }
}
