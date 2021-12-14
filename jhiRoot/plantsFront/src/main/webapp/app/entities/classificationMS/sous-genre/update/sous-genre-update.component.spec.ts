jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SousGenreService } from '../service/sous-genre.service';
import { ISousGenre, SousGenre } from '../sous-genre.model';
import { IGenre } from 'app/entities/classificationMS/genre/genre.model';
import { GenreService } from 'app/entities/classificationMS/genre/service/genre.service';

import { SousGenreUpdateComponent } from './sous-genre-update.component';

describe('SousGenre Management Update Component', () => {
  let comp: SousGenreUpdateComponent;
  let fixture: ComponentFixture<SousGenreUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sousGenreService: SousGenreService;
  let genreService: GenreService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SousGenreUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SousGenreUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SousGenreUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sousGenreService = TestBed.inject(SousGenreService);
    genreService = TestBed.inject(GenreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SousGenre query and add missing value', () => {
      const sousGenre: ISousGenre = { id: 456 };
      const sousGenre: ISousGenre = { id: 91100 };
      sousGenre.sousGenre = sousGenre;

      const sousGenreCollection: ISousGenre[] = [{ id: 28397 }];
      jest.spyOn(sousGenreService, 'query').mockReturnValue(of(new HttpResponse({ body: sousGenreCollection })));
      const additionalSousGenres = [sousGenre];
      const expectedCollection: ISousGenre[] = [...additionalSousGenres, ...sousGenreCollection];
      jest.spyOn(sousGenreService, 'addSousGenreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousGenre });
      comp.ngOnInit();

      expect(sousGenreService.query).toHaveBeenCalled();
      expect(sousGenreService.addSousGenreToCollectionIfMissing).toHaveBeenCalledWith(sousGenreCollection, ...additionalSousGenres);
      expect(comp.sousGenresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Genre query and add missing value', () => {
      const sousGenre: ISousGenre = { id: 456 };
      const genre: IGenre = { id: 70767 };
      sousGenre.genre = genre;

      const genreCollection: IGenre[] = [{ id: 30626 }];
      jest.spyOn(genreService, 'query').mockReturnValue(of(new HttpResponse({ body: genreCollection })));
      const additionalGenres = [genre];
      const expectedCollection: IGenre[] = [...additionalGenres, ...genreCollection];
      jest.spyOn(genreService, 'addGenreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousGenre });
      comp.ngOnInit();

      expect(genreService.query).toHaveBeenCalled();
      expect(genreService.addGenreToCollectionIfMissing).toHaveBeenCalledWith(genreCollection, ...additionalGenres);
      expect(comp.genresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sousGenre: ISousGenre = { id: 456 };
      const sousGenre: ISousGenre = { id: 8865 };
      sousGenre.sousGenre = sousGenre;
      const genre: IGenre = { id: 82976 };
      sousGenre.genre = genre;

      activatedRoute.data = of({ sousGenre });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sousGenre));
      expect(comp.sousGenresSharedCollection).toContain(sousGenre);
      expect(comp.genresSharedCollection).toContain(genre);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousGenre>>();
      const sousGenre = { id: 123 };
      jest.spyOn(sousGenreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousGenre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousGenre }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sousGenreService.update).toHaveBeenCalledWith(sousGenre);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousGenre>>();
      const sousGenre = new SousGenre();
      jest.spyOn(sousGenreService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousGenre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousGenre }));
      saveSubject.complete();

      // THEN
      expect(sousGenreService.create).toHaveBeenCalledWith(sousGenre);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousGenre>>();
      const sousGenre = { id: 123 };
      jest.spyOn(sousGenreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousGenre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sousGenreService.update).toHaveBeenCalledWith(sousGenre);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSousGenreById', () => {
      it('Should return tracked SousGenre primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousGenreById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackGenreById', () => {
      it('Should return tracked Genre primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackGenreById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
