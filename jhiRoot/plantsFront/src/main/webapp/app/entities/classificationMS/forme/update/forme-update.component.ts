import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IForme, Forme } from '../forme.model';
import { FormeService } from '../service/forme.service';
import { ISousVariete } from 'app/entities/classificationMS/sous-variete/sous-variete.model';
import { SousVarieteService } from 'app/entities/classificationMS/sous-variete/service/sous-variete.service';

@Component({
  selector: 'perma-forme-update',
  templateUrl: './forme-update.component.html',
})
export class FormeUpdateComponent implements OnInit {
  isSaving = false;

  formesSharedCollection: IForme[] = [];
  sousVarietesSharedCollection: ISousVariete[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    sousVariete: [],
    forme: [],
  });

  constructor(
    protected formeService: FormeService,
    protected sousVarieteService: SousVarieteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ forme }) => {
      this.updateForm(forme);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const forme = this.createFromForm();
    if (forme.id !== undefined) {
      this.subscribeToSaveResponse(this.formeService.update(forme));
    } else {
      this.subscribeToSaveResponse(this.formeService.create(forme));
    }
  }

  trackFormeById(index: number, item: IForme): number {
    return item.id!;
  }

  trackSousVarieteById(index: number, item: ISousVariete): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IForme>>): void {
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

  protected updateForm(forme: IForme): void {
    this.editForm.patchValue({
      id: forme.id,
      nomFr: forme.nomFr,
      nomLatin: forme.nomLatin,
      sousVariete: forme.sousVariete,
      forme: forme.forme,
    });

    this.formesSharedCollection = this.formeService.addFormeToCollectionIfMissing(this.formesSharedCollection, forme.forme);
    this.sousVarietesSharedCollection = this.sousVarieteService.addSousVarieteToCollectionIfMissing(
      this.sousVarietesSharedCollection,
      forme.sousVariete
    );
  }

  protected loadRelationshipsOptions(): void {
    this.formeService
      .query()
      .pipe(map((res: HttpResponse<IForme[]>) => res.body ?? []))
      .pipe(map((formes: IForme[]) => this.formeService.addFormeToCollectionIfMissing(formes, this.editForm.get('forme')!.value)))
      .subscribe((formes: IForme[]) => (this.formesSharedCollection = formes));

    this.sousVarieteService
      .query()
      .pipe(map((res: HttpResponse<ISousVariete[]>) => res.body ?? []))
      .pipe(
        map((sousVarietes: ISousVariete[]) =>
          this.sousVarieteService.addSousVarieteToCollectionIfMissing(sousVarietes, this.editForm.get('sousVariete')!.value)
        )
      )
      .subscribe((sousVarietes: ISousVariete[]) => (this.sousVarietesSharedCollection = sousVarietes));
  }

  protected createFromForm(): IForme {
    return {
      ...new Forme(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      sousVariete: this.editForm.get(['sousVariete'])!.value,
      forme: this.editForm.get(['forme'])!.value,
    };
  }
}
