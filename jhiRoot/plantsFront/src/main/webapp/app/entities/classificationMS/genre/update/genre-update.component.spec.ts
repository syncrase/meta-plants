jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GenreService } from '../service/genre.service';
import { IGenre, Genre } from '../genre.model';
import { ISousTribu } from 'app/entities/classificationMS/sous-tribu/sous-tribu.model';
import { SousTribuService } from 'app/entities/classificationMS/sous-tribu/service/sous-tribu.service';

import { GenreUpdateComponent } from './genre-update.component';

describe('Genre Management Update Component', () => {
  let comp: GenreUpdateComponent;
  let fixture: ComponentFixture<GenreUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let genreService: GenreService;
  let sousTribuService: SousTribuService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [GenreUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(GenreUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GenreUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    genreService = TestBed.inject(GenreService);
    sousTribuService = TestBed.inject(SousTribuService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Genre query and add missing value', () => {
      const genre: IGenre = { id: 456 };
      const genre: IGenre = { id: 31243 };
      genre.genre = genre;

      const genreCollection: IGenre[] = [{ id: 75717 }];
      jest.spyOn(genreService, 'query').mockReturnValue(of(new HttpResponse({ body: genreCollection })));
      const additionalGenres = [genre];
      const expectedCollection: IGenre[] = [...additionalGenres, ...genreCollection];
      jest.spyOn(genreService, 'addGenreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ genre });
      comp.ngOnInit();

      expect(genreService.query).toHaveBeenCalled();
      expect(genreService.addGenreToCollectionIfMissing).toHaveBeenCalledWith(genreCollection, ...additionalGenres);
      expect(comp.genresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SousTribu query and add missing value', () => {
      const genre: IGenre = { id: 456 };
      const sousTribu: ISousTribu = { id: 29866 };
      genre.sousTribu = sousTribu;

      const sousTribuCollection: ISousTribu[] = [{ id: 38745 }];
      jest.spyOn(sousTribuService, 'query').mockReturnValue(of(new HttpResponse({ body: sousTribuCollection })));
      const additionalSousTribus = [sousTribu];
      const expectedCollection: ISousTribu[] = [...additionalSousTribus, ...sousTribuCollection];
      jest.spyOn(sousTribuService, 'addSousTribuToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ genre });
      comp.ngOnInit();

      expect(sousTribuService.query).toHaveBeenCalled();
      expect(sousTribuService.addSousTribuToCollectionIfMissing).toHaveBeenCalledWith(sousTribuCollection, ...additionalSousTribus);
      expect(comp.sousTribusSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const genre: IGenre = { id: 456 };
      const genre: IGenre = { id: 52687 };
      genre.genre = genre;
      const sousTribu: ISousTribu = { id: 97392 };
      genre.sousTribu = sousTribu;

      activatedRoute.data = of({ genre });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(genre));
      expect(comp.genresSharedCollection).toContain(genre);
      expect(comp.sousTribusSharedCollection).toContain(sousTribu);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Genre>>();
      const genre = { id: 123 };
      jest.spyOn(genreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ genre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: genre }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(genreService.update).toHaveBeenCalledWith(genre);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Genre>>();
      const genre = new Genre();
      jest.spyOn(genreService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ genre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: genre }));
      saveSubject.complete();

      // THEN
      expect(genreService.create).toHaveBeenCalledWith(genre);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Genre>>();
      const genre = { id: 123 };
      jest.spyOn(genreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ genre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(genreService.update).toHaveBeenCalledWith(genre);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackGenreById', () => {
      it('Should return tracked Genre primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackGenreById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSousTribuById', () => {
      it('Should return tracked SousTribu primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousTribuById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
