import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISousForme, SousForme } from '../sous-forme.model';
import { SousFormeService } from '../service/sous-forme.service';
import { IForme } from 'app/entities/classificationMS/forme/forme.model';
import { FormeService } from 'app/entities/classificationMS/forme/service/forme.service';

@Component({
  selector: 'perma-sous-forme-update',
  templateUrl: './sous-forme-update.component.html',
})
export class SousFormeUpdateComponent implements OnInit {
  isSaving = false;

  sousFormesSharedCollection: ISousForme[] = [];
  formesSharedCollection: IForme[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    forme: [],
    sousForme: [],
  });

  constructor(
    protected sousFormeService: SousFormeService,
    protected formeService: FormeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousForme }) => {
      this.updateForm(sousForme);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sousForme = this.createFromForm();
    if (sousForme.id !== undefined) {
      this.subscribeToSaveResponse(this.sousFormeService.update(sousForme));
    } else {
      this.subscribeToSaveResponse(this.sousFormeService.create(sousForme));
    }
  }

  trackSousFormeById(index: number, item: ISousForme): number {
    return item.id!;
  }

  trackFormeById(index: number, item: IForme): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISousForme>>): void {
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

  protected updateForm(sousForme: ISousForme): void {
    this.editForm.patchValue({
      id: sousForme.id,
      nomFr: sousForme.nomFr,
      nomLatin: sousForme.nomLatin,
      forme: sousForme.forme,
      sousForme: sousForme.sousForme,
    });

    this.sousFormesSharedCollection = this.sousFormeService.addSousFormeToCollectionIfMissing(
      this.sousFormesSharedCollection,
      sousForme.sousForme
    );
    this.formesSharedCollection = this.formeService.addFormeToCollectionIfMissing(this.formesSharedCollection, sousForme.forme);
  }

  protected loadRelationshipsOptions(): void {
    this.sousFormeService
      .query()
      .pipe(map((res: HttpResponse<ISousForme[]>) => res.body ?? []))
      .pipe(
        map((sousFormes: ISousForme[]) =>
          this.sousFormeService.addSousFormeToCollectionIfMissing(sousFormes, this.editForm.get('sousForme')!.value)
        )
      )
      .subscribe((sousFormes: ISousForme[]) => (this.sousFormesSharedCollection = sousFormes));

    this.formeService
      .query()
      .pipe(map((res: HttpResponse<IForme[]>) => res.body ?? []))
      .pipe(map((formes: IForme[]) => this.formeService.addFormeToCollectionIfMissing(formes, this.editForm.get('forme')!.value)))
      .subscribe((formes: IForme[]) => (this.formesSharedCollection = formes));
  }

  protected createFromForm(): ISousForme {
    return {
      ...new SousForme(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      forme: this.editForm.get(['forme'])!.value,
      sousForme: this.editForm.get(['sousForme'])!.value,
    };
  }
}
