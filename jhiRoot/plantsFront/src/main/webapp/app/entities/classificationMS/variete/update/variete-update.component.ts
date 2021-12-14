import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVariete, Variete } from '../variete.model';
import { VarieteService } from '../service/variete.service';
import { ISousEspece } from 'app/entities/classificationMS/sous-espece/sous-espece.model';
import { SousEspeceService } from 'app/entities/classificationMS/sous-espece/service/sous-espece.service';

@Component({
  selector: 'perma-variete-update',
  templateUrl: './variete-update.component.html',
})
export class VarieteUpdateComponent implements OnInit {
  isSaving = false;

  varietesSharedCollection: IVariete[] = [];
  sousEspecesSharedCollection: ISousEspece[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    sousEspece: [],
    variete: [],
  });

  constructor(
    protected varieteService: VarieteService,
    protected sousEspeceService: SousEspeceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ variete }) => {
      this.updateForm(variete);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const variete = this.createFromForm();
    if (variete.id !== undefined) {
      this.subscribeToSaveResponse(this.varieteService.update(variete));
    } else {
      this.subscribeToSaveResponse(this.varieteService.create(variete));
    }
  }

  trackVarieteById(index: number, item: IVariete): number {
    return item.id!;
  }

  trackSousEspeceById(index: number, item: ISousEspece): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVariete>>): void {
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

  protected updateForm(variete: IVariete): void {
    this.editForm.patchValue({
      id: variete.id,
      nomFr: variete.nomFr,
      nomLatin: variete.nomLatin,
      sousEspece: variete.sousEspece,
      variete: variete.variete,
    });

    this.varietesSharedCollection = this.varieteService.addVarieteToCollectionIfMissing(this.varietesSharedCollection, variete.variete);
    this.sousEspecesSharedCollection = this.sousEspeceService.addSousEspeceToCollectionIfMissing(
      this.sousEspecesSharedCollection,
      variete.sousEspece
    );
  }

  protected loadRelationshipsOptions(): void {
    this.varieteService
      .query()
      .pipe(map((res: HttpResponse<IVariete[]>) => res.body ?? []))
      .pipe(
        map((varietes: IVariete[]) => this.varieteService.addVarieteToCollectionIfMissing(varietes, this.editForm.get('variete')!.value))
      )
      .subscribe((varietes: IVariete[]) => (this.varietesSharedCollection = varietes));

    this.sousEspeceService
      .query()
      .pipe(map((res: HttpResponse<ISousEspece[]>) => res.body ?? []))
      .pipe(
        map((sousEspeces: ISousEspece[]) =>
          this.sousEspeceService.addSousEspeceToCollectionIfMissing(sousEspeces, this.editForm.get('sousEspece')!.value)
        )
      )
      .subscribe((sousEspeces: ISousEspece[]) => (this.sousEspecesSharedCollection = sousEspeces));
  }

  protected createFromForm(): IVariete {
    return {
      ...new Variete(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      sousEspece: this.editForm.get(['sousEspece'])!.value,
      variete: this.editForm.get(['variete'])!.value,
    };
  }
}
