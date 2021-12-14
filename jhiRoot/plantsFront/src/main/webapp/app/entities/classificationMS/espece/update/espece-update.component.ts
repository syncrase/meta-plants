import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEspece, Espece } from '../espece.model';
import { EspeceService } from '../service/espece.service';
import { ISousSection } from 'app/entities/classificationMS/sous-section/sous-section.model';
import { SousSectionService } from 'app/entities/classificationMS/sous-section/service/sous-section.service';

@Component({
  selector: 'perma-espece-update',
  templateUrl: './espece-update.component.html',
})
export class EspeceUpdateComponent implements OnInit {
  isSaving = false;

  especesSharedCollection: IEspece[] = [];
  sousSectionsSharedCollection: ISousSection[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    sousSection: [],
    espece: [],
  });

  constructor(
    protected especeService: EspeceService,
    protected sousSectionService: SousSectionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ espece }) => {
      this.updateForm(espece);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const espece = this.createFromForm();
    if (espece.id !== undefined) {
      this.subscribeToSaveResponse(this.especeService.update(espece));
    } else {
      this.subscribeToSaveResponse(this.especeService.create(espece));
    }
  }

  trackEspeceById(index: number, item: IEspece): number {
    return item.id!;
  }

  trackSousSectionById(index: number, item: ISousSection): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEspece>>): void {
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

  protected updateForm(espece: IEspece): void {
    this.editForm.patchValue({
      id: espece.id,
      nomFr: espece.nomFr,
      nomLatin: espece.nomLatin,
      sousSection: espece.sousSection,
      espece: espece.espece,
    });

    this.especesSharedCollection = this.especeService.addEspeceToCollectionIfMissing(this.especesSharedCollection, espece.espece);
    this.sousSectionsSharedCollection = this.sousSectionService.addSousSectionToCollectionIfMissing(
      this.sousSectionsSharedCollection,
      espece.sousSection
    );
  }

  protected loadRelationshipsOptions(): void {
    this.especeService
      .query()
      .pipe(map((res: HttpResponse<IEspece[]>) => res.body ?? []))
      .pipe(map((especes: IEspece[]) => this.especeService.addEspeceToCollectionIfMissing(especes, this.editForm.get('espece')!.value)))
      .subscribe((especes: IEspece[]) => (this.especesSharedCollection = especes));

    this.sousSectionService
      .query()
      .pipe(map((res: HttpResponse<ISousSection[]>) => res.body ?? []))
      .pipe(
        map((sousSections: ISousSection[]) =>
          this.sousSectionService.addSousSectionToCollectionIfMissing(sousSections, this.editForm.get('sousSection')!.value)
        )
      )
      .subscribe((sousSections: ISousSection[]) => (this.sousSectionsSharedCollection = sousSections));
  }

  protected createFromForm(): IEspece {
    return {
      ...new Espece(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      sousSection: this.editForm.get(['sousSection'])!.value,
      espece: this.editForm.get(['espece'])!.value,
    };
  }
}
