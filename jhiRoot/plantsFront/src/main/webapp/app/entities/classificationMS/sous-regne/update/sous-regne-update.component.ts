import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISousRegne, SousRegne } from '../sous-regne.model';
import { SousRegneService } from '../service/sous-regne.service';
import { IRegne } from 'app/entities/classificationMS/regne/regne.model';
import { RegneService } from 'app/entities/classificationMS/regne/service/regne.service';

@Component({
  selector: 'perma-sous-regne-update',
  templateUrl: './sous-regne-update.component.html',
})
export class SousRegneUpdateComponent implements OnInit {
  isSaving = false;

  sousRegnesSharedCollection: ISousRegne[] = [];
  regnesSharedCollection: IRegne[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    regne: [],
    sousRegne: [],
  });

  constructor(
    protected sousRegneService: SousRegneService,
    protected regneService: RegneService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousRegne }) => {
      this.updateForm(sousRegne);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sousRegne = this.createFromForm();
    if (sousRegne.id !== undefined) {
      this.subscribeToSaveResponse(this.sousRegneService.update(sousRegne));
    } else {
      this.subscribeToSaveResponse(this.sousRegneService.create(sousRegne));
    }
  }

  trackSousRegneById(index: number, item: ISousRegne): number {
    return item.id!;
  }

  trackRegneById(index: number, item: IRegne): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISousRegne>>): void {
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

  protected updateForm(sousRegne: ISousRegne): void {
    this.editForm.patchValue({
      id: sousRegne.id,
      nomFr: sousRegne.nomFr,
      nomLatin: sousRegne.nomLatin,
      regne: sousRegne.regne,
      sousRegne: sousRegne.sousRegne,
    });

    this.sousRegnesSharedCollection = this.sousRegneService.addSousRegneToCollectionIfMissing(
      this.sousRegnesSharedCollection,
      sousRegne.sousRegne
    );
    this.regnesSharedCollection = this.regneService.addRegneToCollectionIfMissing(this.regnesSharedCollection, sousRegne.regne);
  }

  protected loadRelationshipsOptions(): void {
    this.sousRegneService
      .query()
      .pipe(map((res: HttpResponse<ISousRegne[]>) => res.body ?? []))
      .pipe(
        map((sousRegnes: ISousRegne[]) =>
          this.sousRegneService.addSousRegneToCollectionIfMissing(sousRegnes, this.editForm.get('sousRegne')!.value)
        )
      )
      .subscribe((sousRegnes: ISousRegne[]) => (this.sousRegnesSharedCollection = sousRegnes));

    this.regneService
      .query()
      .pipe(map((res: HttpResponse<IRegne[]>) => res.body ?? []))
      .pipe(map((regnes: IRegne[]) => this.regneService.addRegneToCollectionIfMissing(regnes, this.editForm.get('regne')!.value)))
      .subscribe((regnes: IRegne[]) => (this.regnesSharedCollection = regnes));
  }

  protected createFromForm(): ISousRegne {
    return {
      ...new SousRegne(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      regne: this.editForm.get(['regne'])!.value,
      sousRegne: this.editForm.get(['sousRegne'])!.value,
    };
  }
}
