import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISection, Section } from '../section.model';
import { SectionService } from '../service/section.service';
import { ISousGenre } from 'app/entities/classificationMS/sous-genre/sous-genre.model';
import { SousGenreService } from 'app/entities/classificationMS/sous-genre/service/sous-genre.service';

@Component({
  selector: 'perma-section-update',
  templateUrl: './section-update.component.html',
})
export class SectionUpdateComponent implements OnInit {
  isSaving = false;

  sectionsSharedCollection: ISection[] = [];
  sousGenresSharedCollection: ISousGenre[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    sousGenre: [],
    section: [],
  });

  constructor(
    protected sectionService: SectionService,
    protected sousGenreService: SousGenreService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ section }) => {
      this.updateForm(section);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const section = this.createFromForm();
    if (section.id !== undefined) {
      this.subscribeToSaveResponse(this.sectionService.update(section));
    } else {
      this.subscribeToSaveResponse(this.sectionService.create(section));
    }
  }

  trackSectionById(index: number, item: ISection): number {
    return item.id!;
  }

  trackSousGenreById(index: number, item: ISousGenre): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISection>>): void {
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

  protected updateForm(section: ISection): void {
    this.editForm.patchValue({
      id: section.id,
      nomFr: section.nomFr,
      nomLatin: section.nomLatin,
      sousGenre: section.sousGenre,
      section: section.section,
    });

    this.sectionsSharedCollection = this.sectionService.addSectionToCollectionIfMissing(this.sectionsSharedCollection, section.section);
    this.sousGenresSharedCollection = this.sousGenreService.addSousGenreToCollectionIfMissing(
      this.sousGenresSharedCollection,
      section.sousGenre
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sectionService
      .query()
      .pipe(map((res: HttpResponse<ISection[]>) => res.body ?? []))
      .pipe(
        map((sections: ISection[]) => this.sectionService.addSectionToCollectionIfMissing(sections, this.editForm.get('section')!.value))
      )
      .subscribe((sections: ISection[]) => (this.sectionsSharedCollection = sections));

    this.sousGenreService
      .query()
      .pipe(map((res: HttpResponse<ISousGenre[]>) => res.body ?? []))
      .pipe(
        map((sousGenres: ISousGenre[]) =>
          this.sousGenreService.addSousGenreToCollectionIfMissing(sousGenres, this.editForm.get('sousGenre')!.value)
        )
      )
      .subscribe((sousGenres: ISousGenre[]) => (this.sousGenresSharedCollection = sousGenres));
  }

  protected createFromForm(): ISection {
    return {
      ...new Section(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      sousGenre: this.editForm.get(['sousGenre'])!.value,
      section: this.editForm.get(['section'])!.value,
    };
  }
}
