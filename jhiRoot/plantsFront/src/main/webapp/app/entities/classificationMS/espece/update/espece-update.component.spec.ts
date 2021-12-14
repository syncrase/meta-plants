jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EspeceService } from '../service/espece.service';
import { IEspece, Espece } from '../espece.model';
import { ISousSection } from 'app/entities/classificationMS/sous-section/sous-section.model';
import { SousSectionService } from 'app/entities/classificationMS/sous-section/service/sous-section.service';

import { EspeceUpdateComponent } from './espece-update.component';

describe('Espece Management Update Component', () => {
  let comp: EspeceUpdateComponent;
  let fixture: ComponentFixture<EspeceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let especeService: EspeceService;
  let sousSectionService: SousSectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EspeceUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(EspeceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EspeceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    especeService = TestBed.inject(EspeceService);
    sousSectionService = TestBed.inject(SousSectionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Espece query and add missing value', () => {
      const espece: IEspece = { id: 456 };
      const espece: IEspece = { id: 48558 };
      espece.espece = espece;

      const especeCollection: IEspece[] = [{ id: 25645 }];
      jest.spyOn(especeService, 'query').mockReturnValue(of(new HttpResponse({ body: especeCollection })));
      const additionalEspeces = [espece];
      const expectedCollection: IEspece[] = [...additionalEspeces, ...especeCollection];
      jest.spyOn(especeService, 'addEspeceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ espece });
      comp.ngOnInit();

      expect(especeService.query).toHaveBeenCalled();
      expect(especeService.addEspeceToCollectionIfMissing).toHaveBeenCalledWith(especeCollection, ...additionalEspeces);
      expect(comp.especesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SousSection query and add missing value', () => {
      const espece: IEspece = { id: 456 };
      const sousSection: ISousSection = { id: 13863 };
      espece.sousSection = sousSection;

      const sousSectionCollection: ISousSection[] = [{ id: 15713 }];
      jest.spyOn(sousSectionService, 'query').mockReturnValue(of(new HttpResponse({ body: sousSectionCollection })));
      const additionalSousSections = [sousSection];
      const expectedCollection: ISousSection[] = [...additionalSousSections, ...sousSectionCollection];
      jest.spyOn(sousSectionService, 'addSousSectionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ espece });
      comp.ngOnInit();

      expect(sousSectionService.query).toHaveBeenCalled();
      expect(sousSectionService.addSousSectionToCollectionIfMissing).toHaveBeenCalledWith(sousSectionCollection, ...additionalSousSections);
      expect(comp.sousSectionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const espece: IEspece = { id: 456 };
      const espece: IEspece = { id: 1702 };
      espece.espece = espece;
      const sousSection: ISousSection = { id: 73856 };
      espece.sousSection = sousSection;

      activatedRoute.data = of({ espece });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(espece));
      expect(comp.especesSharedCollection).toContain(espece);
      expect(comp.sousSectionsSharedCollection).toContain(sousSection);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Espece>>();
      const espece = { id: 123 };
      jest.spyOn(especeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ espece });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: espece }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(especeService.update).toHaveBeenCalledWith(espece);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Espece>>();
      const espece = new Espece();
      jest.spyOn(especeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ espece });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: espece }));
      saveSubject.complete();

      // THEN
      expect(especeService.create).toHaveBeenCalledWith(espece);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Espece>>();
      const espece = { id: 123 };
      jest.spyOn(especeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ espece });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(especeService.update).toHaveBeenCalledWith(espece);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEspeceById', () => {
      it('Should return tracked Espece primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEspeceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSousSectionById', () => {
      it('Should return tracked SousSection primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousSectionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
