import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISousGenre, SousGenre } from '../sous-genre.model';
import { SousGenreService } from '../service/sous-genre.service';
import { IGenre } from 'app/entities/classificationMS/genre/genre.model';
import { GenreService } from 'app/entities/classificationMS/genre/service/genre.service';

@Component({
  selector: 'perma-sous-genre-update',
  templateUrl: './sous-genre-update.component.html',
})
export class SousGenreUpdateComponent implements OnInit {
  isSaving = false;

  sousGenresSharedCollection: ISousGenre[] = [];
  genresSharedCollection: IGenre[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    genre: [],
    sousGenre: [],
  });

  constructor(
    protected sousGenreService: SousGenreService,
    protected genreService: GenreService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousGenre }) => {
      this.updateForm(sousGenre);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sousGenre = this.createFromForm();
    if (sousGenre.id !== undefined) {
      this.subscribeToSaveResponse(this.sousGenreService.update(sousGenre));
    } else {
      this.subscribeToSaveResponse(this.sousGenreService.create(sousGenre));
    }
  }

  trackSousGenreById(index: number, item: ISousGenre): number {
    return item.id!;
  }

  trackGenreById(index: number, item: IGenre): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISousGenre>>): void {
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

  protected updateForm(sousGenre: ISousGenre): void {
    this.editForm.patchValue({
      id: sousGenre.id,
      nomFr: sousGenre.nomFr,
      nomLatin: sousGenre.nomLatin,
      genre: sousGenre.genre,
      sousGenre: sousGenre.sousGenre,
    });

    this.sousGenresSharedCollection = this.sousGenreService.addSousGenreToCollectionIfMissing(
      this.sousGenresSharedCollection,
      sousGenre.sousGenre
    );
    this.genresSharedCollection = this.genreService.addGenreToCollectionIfMissing(this.genresSharedCollection, sousGenre.genre);
  }

  protected loadRelationshipsOptions(): void {
    this.sousGenreService
      .query()
      .pipe(map((res: HttpResponse<ISousGenre[]>) => res.body ?? []))
      .pipe(
        map((sousGenres: ISousGenre[]) =>
          this.sousGenreService.addSousGenreToCollectionIfMissing(sousGenres, this.editForm.get('sousGenre')!.value)
        )
      )
      .subscribe((sousGenres: ISousGenre[]) => (this.sousGenresSharedCollection = sousGenres));

    this.genreService
      .query()
      .pipe(map((res: HttpResponse<IGenre[]>) => res.body ?? []))
      .pipe(map((genres: IGenre[]) => this.genreService.addGenreToCollectionIfMissing(genres, this.editForm.get('genre')!.value)))
      .subscribe((genres: IGenre[]) => (this.genresSharedCollection = genres));
  }

  protected createFromForm(): ISousGenre {
    return {
      ...new SousGenre(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      genre: this.editForm.get(['genre'])!.value,
      sousGenre: this.editForm.get(['sousGenre'])!.value,
    };
  }
}
