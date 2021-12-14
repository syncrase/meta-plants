import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISousSection, SousSection } from '../sous-section.model';
import { SousSectionService } from '../service/sous-section.service';
import { ISection } from 'app/entities/classificationMS/section/section.model';
import { SectionService } from 'app/entities/classificationMS/section/service/section.service';

@Component({
  selector: 'perma-sous-section-update',
  templateUrl: './sous-section-update.component.html',
})
export class SousSectionUpdateComponent implements OnInit {
  isSaving = false;

  sousSectionsSharedCollection: ISousSection[] = [];
  sectionsSharedCollection: ISection[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    section: [],
    sousSection: [],
  });

  constructor(
    protected sousSectionService: SousSectionService,
    protected sectionService: SectionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousSection }) => {
      this.updateForm(sousSection);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sousSection = this.createFromForm();
    if (sousSection.id !== undefined) {
      this.subscribeToSaveResponse(this.sousSectionService.update(sousSection));
    } else {
      this.subscribeToSaveResponse(this.sousSectionService.create(sousSection));
    }
  }

  trackSousSectionById(index: number, item: ISousSection): number {
    return item.id!;
  }

  trackSectionById(index: number, item: ISection): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISousSection>>): void {
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

  protected updateForm(sousSection: ISousSection): void {
    this.editForm.patchValue({
      id: sousSection.id,
      nomFr: sousSection.nomFr,
      nomLatin: sousSection.nomLatin,
      section: sousSection.section,
      sousSection: sousSection.sousSection,
    });

    this.sousSectionsSharedCollection = this.sousSectionService.addSousSectionToCollectionIfMissing(
      this.sousSectionsSharedCollection,
      sousSection.sousSection
    );
    this.sectionsSharedCollection = this.sectionService.addSectionToCollectionIfMissing(this.sectionsSharedCollection, sousSection.section);
  }

  protected loadRelationshipsOptions(): void {
    this.sousSectionService
      .query()
      .pipe(map((res: HttpResponse<ISousSection[]>) => res.body ?? []))
      .pipe(
        map((sousSections: ISousSection[]) =>
          this.sousSectionService.addSousSectionToCollectionIfMissing(sousSections, this.editForm.get('sousSection')!.value)
        )
      )
      .subscribe((sousSections: ISousSection[]) => (this.sousSectionsSharedCollection = sousSections));

    this.sectionService
      .query()
      .pipe(map((res: HttpResponse<ISection[]>) => res.body ?? []))
      .pipe(
        map((sections: ISection[]) => this.sectionService.addSectionToCollectionIfMissing(sections, this.editForm.get('section')!.value))
      )
      .subscribe((sections: ISection[]) => (this.sectionsSharedCollection = sections));
  }

  protected createFromForm(): ISousSection {
    return {
      ...new SousSection(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      section: this.editForm.get(['section'])!.value,
      sousSection: this.editForm.get(['sousSection'])!.value,
    };
  }
}
