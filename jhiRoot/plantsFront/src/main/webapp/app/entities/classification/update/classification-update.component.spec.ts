jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClassificationService } from '../service/classification.service';
import { IClassification, Classification } from '../classification.model';
import { IRaunkier } from 'app/entities/raunkier/raunkier.model';
import { RaunkierService } from 'app/entities/raunkier/service/raunkier.service';
import { ICronquist } from 'app/entities/cronquist/cronquist.model';
import { CronquistService } from 'app/entities/cronquist/service/cronquist.service';
import { IAPGI } from 'app/entities/apgi/apgi.model';
import { APGIService } from 'app/entities/apgi/service/apgi.service';
import { IAPGII } from 'app/entities/apgii/apgii.model';
import { APGIIService } from 'app/entities/apgii/service/apgii.service';
import { IAPGIII } from 'app/entities/apgiii/apgiii.model';
import { APGIIIService } from 'app/entities/apgiii/service/apgiii.service';
import { IAPGIV } from 'app/entities/apgiv/apgiv.model';
import { APGIVService } from 'app/entities/apgiv/service/apgiv.service';

import { ClassificationUpdateComponent } from './classification-update.component';

describe('Classification Management Update Component', () => {
  let comp: ClassificationUpdateComponent;
  let fixture: ComponentFixture<ClassificationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let classificationService: ClassificationService;
  let raunkierService: RaunkierService;
  let cronquistService: CronquistService;
  let aPGIService: APGIService;
  let aPGIIService: APGIIService;
  let aPGIIIService: APGIIIService;
  let aPGIVService: APGIVService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ClassificationUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ClassificationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClassificationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    classificationService = TestBed.inject(ClassificationService);
    raunkierService = TestBed.inject(RaunkierService);
    cronquistService = TestBed.inject(CronquistService);
    aPGIService = TestBed.inject(APGIService);
    aPGIIService = TestBed.inject(APGIIService);
    aPGIIIService = TestBed.inject(APGIIIService);
    aPGIVService = TestBed.inject(APGIVService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Raunkier query and add missing value', () => {
      const classification: IClassification = { id: 456 };
      const raunkier: IRaunkier = { id: 61579 };
      classification.raunkier = raunkier;

      const raunkierCollection: IRaunkier[] = [{ id: 36464 }];
      jest.spyOn(raunkierService, 'query').mockReturnValue(of(new HttpResponse({ body: raunkierCollection })));
      const additionalRaunkiers = [raunkier];
      const expectedCollection: IRaunkier[] = [...additionalRaunkiers, ...raunkierCollection];
      jest.spyOn(raunkierService, 'addRaunkierToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      expect(raunkierService.query).toHaveBeenCalled();
      expect(raunkierService.addRaunkierToCollectionIfMissing).toHaveBeenCalledWith(raunkierCollection, ...additionalRaunkiers);
      expect(comp.raunkiersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Cronquist query and add missing value', () => {
      const classification: IClassification = { id: 456 };
      const cronquist: ICronquist = { id: 74922 };
      classification.cronquist = cronquist;

      const cronquistCollection: ICronquist[] = [{ id: 24554 }];
      jest.spyOn(cronquistService, 'query').mockReturnValue(of(new HttpResponse({ body: cronquistCollection })));
      const additionalCronquists = [cronquist];
      const expectedCollection: ICronquist[] = [...additionalCronquists, ...cronquistCollection];
      jest.spyOn(cronquistService, 'addCronquistToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      expect(cronquistService.query).toHaveBeenCalled();
      expect(cronquistService.addCronquistToCollectionIfMissing).toHaveBeenCalledWith(cronquistCollection, ...additionalCronquists);
      expect(comp.cronquistsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call APGI query and add missing value', () => {
      const classification: IClassification = { id: 456 };
      const apg1: IAPGI = { id: 75939 };
      classification.apg1 = apg1;

      const aPGICollection: IAPGI[] = [{ id: 26520 }];
      jest.spyOn(aPGIService, 'query').mockReturnValue(of(new HttpResponse({ body: aPGICollection })));
      const additionalAPGIS = [apg1];
      const expectedCollection: IAPGI[] = [...additionalAPGIS, ...aPGICollection];
      jest.spyOn(aPGIService, 'addAPGIToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      expect(aPGIService.query).toHaveBeenCalled();
      expect(aPGIService.addAPGIToCollectionIfMissing).toHaveBeenCalledWith(aPGICollection, ...additionalAPGIS);
      expect(comp.aPGISSharedCollection).toEqual(expectedCollection);
    });

    it('Should call APGII query and add missing value', () => {
      const classification: IClassification = { id: 456 };
      const apg2: IAPGII = { id: 75387 };
      classification.apg2 = apg2;

      const aPGIICollection: IAPGII[] = [{ id: 86005 }];
      jest.spyOn(aPGIIService, 'query').mockReturnValue(of(new HttpResponse({ body: aPGIICollection })));
      const additionalAPGIIS = [apg2];
      const expectedCollection: IAPGII[] = [...additionalAPGIIS, ...aPGIICollection];
      jest.spyOn(aPGIIService, 'addAPGIIToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      expect(aPGIIService.query).toHaveBeenCalled();
      expect(aPGIIService.addAPGIIToCollectionIfMissing).toHaveBeenCalledWith(aPGIICollection, ...additionalAPGIIS);
      expect(comp.aPGIISSharedCollection).toEqual(expectedCollection);
    });

    it('Should call APGIII query and add missing value', () => {
      const classification: IClassification = { id: 456 };
      const apg3: IAPGIII = { id: 88678 };
      classification.apg3 = apg3;

      const aPGIIICollection: IAPGIII[] = [{ id: 64364 }];
      jest.spyOn(aPGIIIService, 'query').mockReturnValue(of(new HttpResponse({ body: aPGIIICollection })));
      const additionalAPGIIIS = [apg3];
      const expectedCollection: IAPGIII[] = [...additionalAPGIIIS, ...aPGIIICollection];
      jest.spyOn(aPGIIIService, 'addAPGIIIToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      expect(aPGIIIService.query).toHaveBeenCalled();
      expect(aPGIIIService.addAPGIIIToCollectionIfMissing).toHaveBeenCalledWith(aPGIIICollection, ...additionalAPGIIIS);
      expect(comp.aPGIIISSharedCollection).toEqual(expectedCollection);
    });

    it('Should call APGIV query and add missing value', () => {
      const classification: IClassification = { id: 456 };
      const apg4: IAPGIV = { id: 34949 };
      classification.apg4 = apg4;

      const aPGIVCollection: IAPGIV[] = [{ id: 31132 }];
      jest.spyOn(aPGIVService, 'query').mockReturnValue(of(new HttpResponse({ body: aPGIVCollection })));
      const additionalAPGIVS = [apg4];
      const expectedCollection: IAPGIV[] = [...additionalAPGIVS, ...aPGIVCollection];
      jest.spyOn(aPGIVService, 'addAPGIVToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      expect(aPGIVService.query).toHaveBeenCalled();
      expect(aPGIVService.addAPGIVToCollectionIfMissing).toHaveBeenCalledWith(aPGIVCollection, ...additionalAPGIVS);
      expect(comp.aPGIVSSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const classification: IClassification = { id: 456 };
      const raunkier: IRaunkier = { id: 54628 };
      classification.raunkier = raunkier;
      const cronquist: ICronquist = { id: 78116 };
      classification.cronquist = cronquist;
      const apg1: IAPGI = { id: 38527 };
      classification.apg1 = apg1;
      const apg2: IAPGII = { id: 21566 };
      classification.apg2 = apg2;
      const apg3: IAPGIII = { id: 84900 };
      classification.apg3 = apg3;
      const apg4: IAPGIV = { id: 72730 };
      classification.apg4 = apg4;

      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(classification));
      expect(comp.raunkiersSharedCollection).toContain(raunkier);
      expect(comp.cronquistsSharedCollection).toContain(cronquist);
      expect(comp.aPGISSharedCollection).toContain(apg1);
      expect(comp.aPGIISSharedCollection).toContain(apg2);
      expect(comp.aPGIIISSharedCollection).toContain(apg3);
      expect(comp.aPGIVSSharedCollection).toContain(apg4);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Classification>>();
      const classification = { id: 123 };
      jest.spyOn(classificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classification }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(classificationService.update).toHaveBeenCalledWith(classification);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Classification>>();
      const classification = new Classification();
      jest.spyOn(classificationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classification }));
      saveSubject.complete();

      // THEN
      expect(classificationService.create).toHaveBeenCalledWith(classification);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Classification>>();
      const classification = { id: 123 };
      jest.spyOn(classificationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(classificationService.update).toHaveBeenCalledWith(classification);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackRaunkierById', () => {
      it('Should return tracked Raunkier primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRaunkierById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCronquistById', () => {
      it('Should return tracked Cronquist primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCronquistById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAPGIById', () => {
      it('Should return tracked APGI primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAPGIById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAPGIIById', () => {
      it('Should return tracked APGII primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAPGIIById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAPGIIIById', () => {
      it('Should return tracked APGIII primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAPGIIIById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAPGIVById', () => {
      it('Should return tracked APGIV primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAPGIVById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
