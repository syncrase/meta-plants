jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SousSectionService } from '../service/sous-section.service';
import { ISousSection, SousSection } from '../sous-section.model';
import { ISection } from 'app/entities/classificationMS/section/section.model';
import { SectionService } from 'app/entities/classificationMS/section/service/section.service';

import { SousSectionUpdateComponent } from './sous-section-update.component';

describe('SousSection Management Update Component', () => {
  let comp: SousSectionUpdateComponent;
  let fixture: ComponentFixture<SousSectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sousSectionService: SousSectionService;
  let sectionService: SectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SousSectionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SousSectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SousSectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sousSectionService = TestBed.inject(SousSectionService);
    sectionService = TestBed.inject(SectionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SousSection query and add missing value', () => {
      const sousSection: ISousSection = { id: 456 };
      const sousSection: ISousSection = { id: 54050 };
      sousSection.sousSection = sousSection;

      const sousSectionCollection: ISousSection[] = [{ id: 77087 }];
      jest.spyOn(sousSectionService, 'query').mockReturnValue(of(new HttpResponse({ body: sousSectionCollection })));
      const additionalSousSections = [sousSection];
      const expectedCollection: ISousSection[] = [...additionalSousSections, ...sousSectionCollection];
      jest.spyOn(sousSectionService, 'addSousSectionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousSection });
      comp.ngOnInit();

      expect(sousSectionService.query).toHaveBeenCalled();
      expect(sousSectionService.addSousSectionToCollectionIfMissing).toHaveBeenCalledWith(sousSectionCollection, ...additionalSousSections);
      expect(comp.sousSectionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Section query and add missing value', () => {
      const sousSection: ISousSection = { id: 456 };
      const section: ISection = { id: 90711 };
      sousSection.section = section;

      const sectionCollection: ISection[] = [{ id: 74641 }];
      jest.spyOn(sectionService, 'query').mockReturnValue(of(new HttpResponse({ body: sectionCollection })));
      const additionalSections = [section];
      const expectedCollection: ISection[] = [...additionalSections, ...sectionCollection];
      jest.spyOn(sectionService, 'addSectionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousSection });
      comp.ngOnInit();

      expect(sectionService.query).toHaveBeenCalled();
      expect(sectionService.addSectionToCollectionIfMissing).toHaveBeenCalledWith(sectionCollection, ...additionalSections);
      expect(comp.sectionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sousSection: ISousSection = { id: 456 };
      const sousSection: ISousSection = { id: 82890 };
      sousSection.sousSection = sousSection;
      const section: ISection = { id: 4218 };
      sousSection.section = section;

      activatedRoute.data = of({ sousSection });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sousSection));
      expect(comp.sousSectionsSharedCollection).toContain(sousSection);
      expect(comp.sectionsSharedCollection).toContain(section);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousSection>>();
      const sousSection = { id: 123 };
      jest.spyOn(sousSectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousSection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousSection }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sousSectionService.update).toHaveBeenCalledWith(sousSection);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousSection>>();
      const sousSection = new SousSection();
      jest.spyOn(sousSectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousSection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousSection }));
      saveSubject.complete();

      // THEN
      expect(sousSectionService.create).toHaveBeenCalledWith(sousSection);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousSection>>();
      const sousSection = { id: 123 };
      jest.spyOn(sousSectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousSection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sousSectionService.update).toHaveBeenCalledWith(sousSection);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSousSectionById', () => {
      it('Should return tracked SousSection primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousSectionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSectionById', () => {
      it('Should return tracked Section primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSectionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
