import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOrdre, Ordre } from '../ordre.model';
import { OrdreService } from '../service/ordre.service';
import { ISuperOrdre } from 'app/entities/classificationMS/super-ordre/super-ordre.model';
import { SuperOrdreService } from 'app/entities/classificationMS/super-ordre/service/super-ordre.service';

@Component({
  selector: 'perma-ordre-update',
  templateUrl: './ordre-update.component.html',
})
export class OrdreUpdateComponent implements OnInit {
  isSaving = false;

  ordresSharedCollection: IOrdre[] = [];
  superOrdresSharedCollection: ISuperOrdre[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    superOrdre: [],
    ordre: [],
  });

  constructor(
    protected ordreService: OrdreService,
    protected superOrdreService: SuperOrdreService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ordre }) => {
      this.updateForm(ordre);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ordre = this.createFromForm();
    if (ordre.id !== undefined) {
      this.subscribeToSaveResponse(this.ordreService.update(ordre));
    } else {
      this.subscribeToSaveResponse(this.ordreService.create(ordre));
    }
  }

  trackOrdreById(index: number, item: IOrdre): number {
    return item.id!;
  }

  trackSuperOrdreById(index: number, item: ISuperOrdre): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrdre>>): void {
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

  protected updateForm(ordre: IOrdre): void {
    this.editForm.patchValue({
      id: ordre.id,
      nomFr: ordre.nomFr,
      nomLatin: ordre.nomLatin,
      superOrdre: ordre.superOrdre,
      ordre: ordre.ordre,
    });

    this.ordresSharedCollection = this.ordreService.addOrdreToCollectionIfMissing(this.ordresSharedCollection, ordre.ordre);
    this.superOrdresSharedCollection = this.superOrdreService.addSuperOrdreToCollectionIfMissing(
      this.superOrdresSharedCollection,
      ordre.superOrdre
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ordreService
      .query()
      .pipe(map((res: HttpResponse<IOrdre[]>) => res.body ?? []))
      .pipe(map((ordres: IOrdre[]) => this.ordreService.addOrdreToCollectionIfMissing(ordres, this.editForm.get('ordre')!.value)))
      .subscribe((ordres: IOrdre[]) => (this.ordresSharedCollection = ordres));

    this.superOrdreService
      .query()
      .pipe(map((res: HttpResponse<ISuperOrdre[]>) => res.body ?? []))
      .pipe(
        map((superOrdres: ISuperOrdre[]) =>
          this.superOrdreService.addSuperOrdreToCollectionIfMissing(superOrdres, this.editForm.get('superOrdre')!.value)
        )
      )
      .subscribe((superOrdres: ISuperOrdre[]) => (this.superOrdresSharedCollection = superOrdres));
  }

  protected createFromForm(): IOrdre {
    return {
      ...new Ordre(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      superOrdre: this.editForm.get(['superOrdre'])!.value,
      ordre: this.editForm.get(['ordre'])!.value,
    };
  }
}
