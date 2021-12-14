import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISousVariete, SousVariete } from '../sous-variete.model';
import { SousVarieteService } from '../service/sous-variete.service';
import { IVariete } from 'app/entities/classificationMS/variete/variete.model';
import { VarieteService } from 'app/entities/classificationMS/variete/service/variete.service';

@Component({
  selector: 'perma-sous-variete-update',
  templateUrl: './sous-variete-update.component.html',
})
export class SousVarieteUpdateComponent implements OnInit {
  isSaving = false;

  sousVarietesSharedCollection: ISousVariete[] = [];
  varietesSharedCollection: IVariete[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    variete: [],
    sousVariete: [],
  });

  constructor(
    protected sousVarieteService: SousVarieteService,
    protected varieteService: VarieteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousVariete }) => {
      this.updateForm(sousVariete);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sousVariete = this.createFromForm();
    if (sousVariete.id !== undefined) {
      this.subscribeToSaveResponse(this.sousVarieteService.update(sousVariete));
    } else {
      this.subscribeToSaveResponse(this.sousVarieteService.create(sousVariete));
    }
  }

  trackSousVarieteById(index: number, item: ISousVariete): number {
    return item.id!;
  }

  trackVarieteById(index: number, item: IVariete): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISousVariete>>): void {
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

  protected updateForm(sousVariete: ISousVariete): void {
    this.editForm.patchValue({
      id: sousVariete.id,
      nomFr: sousVariete.nomFr,
      nomLatin: sousVariete.nomLatin,
      variete: sousVariete.variete,
      sousVariete: sousVariete.sousVariete,
    });

    this.sousVarietesSharedCollection = this.sousVarieteService.addSousVarieteToCollectionIfMissing(
      this.sousVarietesSharedCollection,
      sousVariete.sousVariete
    );
    this.varietesSharedCollection = this.varieteService.addVarieteToCollectionIfMissing(this.varietesSharedCollection, sousVariete.variete);
  }

  protected loadRelationshipsOptions(): void {
    this.sousVarieteService
      .query()
      .pipe(map((res: HttpResponse<ISousVariete[]>) => res.body ?? []))
      .pipe(
        map((sousVarietes: ISousVariete[]) =>
          this.sousVarieteService.addSousVarieteToCollectionIfMissing(sousVarietes, this.editForm.get('sousVariete')!.value)
        )
      )
      .subscribe((sousVarietes: ISousVariete[]) => (this.sousVarietesSharedCollection = sousVarietes));

    this.varieteService
      .query()
      .pipe(map((res: HttpResponse<IVariete[]>) => res.body ?? []))
      .pipe(
        map((varietes: IVariete[]) => this.varieteService.addVarieteToCollectionIfMissing(varietes, this.editForm.get('variete')!.value))
      )
      .subscribe((varietes: IVariete[]) => (this.varietesSharedCollection = varietes));
  }

  protected createFromForm(): ISousVariete {
    return {
      ...new SousVariete(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      variete: this.editForm.get(['variete'])!.value,
      sousVariete: this.editForm.get(['sousVariete'])!.value,
    };
  }
}
