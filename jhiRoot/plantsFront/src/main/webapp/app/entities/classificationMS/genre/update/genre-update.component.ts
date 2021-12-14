import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IGenre, Genre } from '../genre.model';
import { GenreService } from '../service/genre.service';
import { ISousTribu } from 'app/entities/classificationMS/sous-tribu/sous-tribu.model';
import { SousTribuService } from 'app/entities/classificationMS/sous-tribu/service/sous-tribu.service';

@Component({
  selector: 'perma-genre-update',
  templateUrl: './genre-update.component.html',
})
export class GenreUpdateComponent implements OnInit {
  isSaving = false;

  genresSharedCollection: IGenre[] = [];
  sousTribusSharedCollection: ISousTribu[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    sousTribu: [],
    genre: [],
  });

  constructor(
    protected genreService: GenreService,
    protected sousTribuService: SousTribuService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ genre }) => {
      this.updateForm(genre);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const genre = this.createFromForm();
    if (genre.id !== undefined) {
      this.subscribeToSaveResponse(this.genreService.update(genre));
    } else {
      this.subscribeToSaveResponse(this.genreService.create(genre));
    }
  }

  trackGenreById(index: number, item: IGenre): number {
    return item.id!;
  }

  trackSousTribuById(index: number, item: ISousTribu): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGenre>>): void {
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

  protected updateForm(genre: IGenre): void {
    this.editForm.patchValue({
      id: genre.id,
      nomFr: genre.nomFr,
      nomLatin: genre.nomLatin,
      sousTribu: genre.sousTribu,
      genre: genre.genre,
    });

    this.genresSharedCollection = this.genreService.addGenreToCollectionIfMissing(this.genresSharedCollection, genre.genre);
    this.sousTribusSharedCollection = this.sousTribuService.addSousTribuToCollectionIfMissing(
      this.sousTribusSharedCollection,
      genre.sousTribu
    );
  }

  protected loadRelationshipsOptions(): void {
    this.genreService
      .query()
      .pipe(map((res: HttpResponse<IGenre[]>) => res.body ?? []))
      .pipe(map((genres: IGenre[]) => this.genreService.addGenreToCollectionIfMissing(genres, this.editForm.get('genre')!.value)))
      .subscribe((genres: IGenre[]) => (this.genresSharedCollection = genres));

    this.sousTribuService
      .query()
      .pipe(map((res: HttpResponse<ISousTribu[]>) => res.body ?? []))
      .pipe(
        map((sousTribus: ISousTribu[]) =>
          this.sousTribuService.addSousTribuToCollectionIfMissing(sousTribus, this.editForm.get('sousTribu')!.value)
        )
      )
      .subscribe((sousTribus: ISousTribu[]) => (this.sousTribusSharedCollection = sousTribus));
  }

  protected createFromForm(): IGenre {
    return {
      ...new Genre(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      sousTribu: this.editForm.get(['sousTribu'])!.value,
      genre: this.editForm.get(['genre'])!.value,
    };
  }
}
