import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISousOrdre, SousOrdre } from '../sous-ordre.model';
import { SousOrdreService } from '../service/sous-ordre.service';
import { IOrdre } from 'app/entities/classificationMS/ordre/ordre.model';
import { OrdreService } from 'app/entities/classificationMS/ordre/service/ordre.service';

@Component({
  selector: 'perma-sous-ordre-update',
  templateUrl: './sous-ordre-update.component.html',
})
export class SousOrdreUpdateComponent implements OnInit {
  isSaving = false;

  sousOrdresSharedCollection: ISousOrdre[] = [];
  ordresSharedCollection: IOrdre[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    ordre: [],
    sousOrdre: [],
  });

  constructor(
    protected sousOrdreService: SousOrdreService,
    protected ordreService: OrdreService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousOrdre }) => {
      this.updateForm(sousOrdre);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sousOrdre = this.createFromForm();
    if (sousOrdre.id !== undefined) {
      this.subscribeToSaveResponse(this.sousOrdreService.update(sousOrdre));
    } else {
      this.subscribeToSaveResponse(this.sousOrdreService.create(sousOrdre));
    }
  }

  trackSousOrdreById(index: number, item: ISousOrdre): number {
    return item.id!;
  }

  trackOrdreById(index: number, item: IOrdre): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISousOrdre>>): void {
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

  protected updateForm(sousOrdre: ISousOrdre): void {
    this.editForm.patchValue({
      id: sousOrdre.id,
      nomFr: sousOrdre.nomFr,
      nomLatin: sousOrdre.nomLatin,
      ordre: sousOrdre.ordre,
      sousOrdre: sousOrdre.sousOrdre,
    });

    this.sousOrdresSharedCollection = this.sousOrdreService.addSousOrdreToCollectionIfMissing(
      this.sousOrdresSharedCollection,
      sousOrdre.sousOrdre
    );
    this.ordresSharedCollection = this.ordreService.addOrdreToCollectionIfMissing(this.ordresSharedCollection, sousOrdre.ordre);
  }

  protected loadRelationshipsOptions(): void {
    this.sousOrdreService
      .query()
      .pipe(map((res: HttpResponse<ISousOrdre[]>) => res.body ?? []))
      .pipe(
        map((sousOrdres: ISousOrdre[]) =>
          this.sousOrdreService.addSousOrdreToCollectionIfMissing(sousOrdres, this.editForm.get('sousOrdre')!.value)
        )
      )
      .subscribe((sousOrdres: ISousOrdre[]) => (this.sousOrdresSharedCollection = sousOrdres));

    this.ordreService
      .query()
      .pipe(map((res: HttpResponse<IOrdre[]>) => res.body ?? []))
      .pipe(map((ordres: IOrdre[]) => this.ordreService.addOrdreToCollectionIfMissing(ordres, this.editForm.get('ordre')!.value)))
      .subscribe((ordres: IOrdre[]) => (this.ordresSharedCollection = ordres));
  }

  protected createFromForm(): ISousOrdre {
    return {
      ...new SousOrdre(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      ordre: this.editForm.get(['ordre'])!.value,
      sousOrdre: this.editForm.get(['sousOrdre'])!.value,
    };
  }
}
