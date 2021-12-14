jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SectionService } from '../service/section.service';
import { ISection, Section } from '../section.model';
import { ISousGenre } from 'app/entities/classificationMS/sous-genre/sous-genre.model';
import { SousGenreService } from 'app/entities/classificationMS/sous-genre/service/sous-genre.service';

import { SectionUpdateComponent } from './section-update.component';

describe('Section Management Update Component', () => {
  let comp: SectionUpdateComponent;
  let fixture: ComponentFixture<SectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sectionService: SectionService;
  let sousGenreService: SousGenreService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SectionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sectionService = TestBed.inject(SectionService);
    sousGenreService = TestBed.inject(SousGenreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Section query and add missing value', () => {
      const section: ISection = { id: 456 };
      const section: ISection = { id: 53066 };
      section.section = section;

      const sectionCollection: ISection[] = [{ id: 41281 }];
      jest.spyOn(sectionService, 'query').mockReturnValue(of(new HttpResponse({ body: sectionCollection })));
      const additionalSections = [section];
      const expectedCollection: ISection[] = [...additionalSections, ...sectionCollection];
      jest.spyOn(sectionService, 'addSectionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ section });
      comp.ngOnInit();

      expect(sectionService.query).toHaveBeenCalled();
      expect(sectionService.addSectionToCollectionIfMissing).toHaveBeenCalledWith(sectionCollection, ...additionalSections);
      expect(comp.sectionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SousGenre query and add missing value', () => {
      const section: ISection = { id: 456 };
      const sousGenre: ISousGenre = { id: 93661 };
      section.sousGenre = sousGenre;

      const sousGenreCollection: ISousGenre[] = [{ id: 58086 }];
      jest.spyOn(sousGenreService, 'query').mockReturnValue(of(new HttpResponse({ body: sousGenreCollection })));
      const additionalSousGenres = [sousGenre];
      const expectedCollection: ISousGenre[] = [...additionalSousGenres, ...sousGenreCollection];
      jest.spyOn(sousGenreService, 'addSousGenreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ section });
      comp.ngOnInit();

      expect(sousGenreService.query).toHaveBeenCalled();
      expect(sousGenreService.addSousGenreToCollectionIfMissing).toHaveBeenCalledWith(sousGenreCollection, ...additionalSousGenres);
      expect(comp.sousGenresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const section: ISection = { id: 456 };
      const section: ISection = { id: 72361 };
      section.section = section;
      const sousGenre: ISousGenre = { id: 8352 };
      section.sousGenre = sousGenre;

      activatedRoute.data = of({ section });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(section));
      expect(comp.sectionsSharedCollection).toContain(section);
      expect(comp.sousGenresSharedCollection).toContain(sousGenre);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Section>>();
      const section = { id: 123 };
      jest.spyOn(sectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ section });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: section }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sectionService.update).toHaveBeenCalledWith(section);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Section>>();
      const section = new Section();
      jest.spyOn(sectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ section });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: section }));
      saveSubject.complete();

      // THEN
      expect(sectionService.create).toHaveBeenCalledWith(section);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Section>>();
      const section = { id: 123 };
      jest.spyOn(sectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ section });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sectionService.update).toHaveBeenCalledWith(section);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSectionById', () => {
      it('Should return tracked Section primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSectionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSousGenreById', () => {
      it('Should return tracked SousGenre primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousGenreById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
