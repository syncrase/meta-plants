jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClassificationService } from '../service/classification.service';
import { IClassification, Classification } from '../classification.model';
import { IRaunkierPlante } from 'app/entities/plantsMS/raunkier-plante/raunkier-plante.model';
import { RaunkierPlanteService } from 'app/entities/plantsMS/raunkier-plante/service/raunkier-plante.service';
import { ICronquistPlante } from 'app/entities/plantsMS/cronquist-plante/cronquist-plante.model';
import { CronquistPlanteService } from 'app/entities/plantsMS/cronquist-plante/service/cronquist-plante.service';
import { IAPGIPlante } from 'app/entities/plantsMS/apgi-plante/apgi-plante.model';
import { APGIPlanteService } from 'app/entities/plantsMS/apgi-plante/service/apgi-plante.service';
import { IAPGIIPlante } from 'app/entities/plantsMS/apgii-plante/apgii-plante.model';
import { APGIIPlanteService } from 'app/entities/plantsMS/apgii-plante/service/apgii-plante.service';
import { IAPGIIIPlante } from 'app/entities/plantsMS/apgiii-plante/apgiii-plante.model';
import { APGIIIPlanteService } from 'app/entities/plantsMS/apgiii-plante/service/apgiii-plante.service';
import { IAPGIVPlante } from 'app/entities/plantsMS/apgiv-plante/apgiv-plante.model';
import { APGIVPlanteService } from 'app/entities/plantsMS/apgiv-plante/service/apgiv-plante.service';

import { ClassificationUpdateComponent } from './classification-update.component';

describe('Classification Management Update Component', () => {
  let comp: ClassificationUpdateComponent;
  let fixture: ComponentFixture<ClassificationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let classificationService: ClassificationService;
  let raunkierPlanteService: RaunkierPlanteService;
  let cronquistPlanteService: CronquistPlanteService;
  let aPGIPlanteService: APGIPlanteService;
  let aPGIIPlanteService: APGIIPlanteService;
  let aPGIIIPlanteService: APGIIIPlanteService;
  let aPGIVPlanteService: APGIVPlanteService;

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
    raunkierPlanteService = TestBed.inject(RaunkierPlanteService);
    cronquistPlanteService = TestBed.inject(CronquistPlanteService);
    aPGIPlanteService = TestBed.inject(APGIPlanteService);
    aPGIIPlanteService = TestBed.inject(APGIIPlanteService);
    aPGIIIPlanteService = TestBed.inject(APGIIIPlanteService);
    aPGIVPlanteService = TestBed.inject(APGIVPlanteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call raunkier query and add missing value', () => {
      const classification: IClassification = { id: 456 };
      const raunkier: IRaunkierPlante = { id: 94259 };
      classification.raunkier = raunkier;

      const raunkierCollection: IRaunkierPlante[] = [{ id: 98188 }];
      jest.spyOn(raunkierPlanteService, 'query').mockReturnValue(of(new HttpResponse({ body: raunkierCollection })));
      const expectedCollection: IRaunkierPlante[] = [raunkier, ...raunkierCollection];
      jest.spyOn(raunkierPlanteService, 'addRaunkierPlanteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      expect(raunkierPlanteService.query).toHaveBeenCalled();
      expect(raunkierPlanteService.addRaunkierPlanteToCollectionIfMissing).toHaveBeenCalledWith(raunkierCollection, raunkier);
      expect(comp.raunkiersCollection).toEqual(expectedCollection);
    });

    it('Should call cronquist query and add missing value', () => {
      const classification: IClassification = { id: 456 };
      const cronquist: ICronquistPlante = { id: 5280 };
      classification.cronquist = cronquist;

      const cronquistCollection: ICronquistPlante[] = [{ id: 41564 }];
      jest.spyOn(cronquistPlanteService, 'query').mockReturnValue(of(new HttpResponse({ body: cronquistCollection })));
      const expectedCollection: ICronquistPlante[] = [cronquist, ...cronquistCollection];
      jest.spyOn(cronquistPlanteService, 'addCronquistPlanteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      expect(cronquistPlanteService.query).toHaveBeenCalled();
      expect(cronquistPlanteService.addCronquistPlanteToCollectionIfMissing).toHaveBeenCalledWith(cronquistCollection, cronquist);
      expect(comp.cronquistsCollection).toEqual(expectedCollection);
    });

    it('Should call apg1 query and add missing value', () => {
      const classification: IClassification = { id: 456 };
      const apg1: IAPGIPlante = { id: 38719 };
      classification.apg1 = apg1;

      const apg1Collection: IAPGIPlante[] = [{ id: 82341 }];
      jest.spyOn(aPGIPlanteService, 'query').mockReturnValue(of(new HttpResponse({ body: apg1Collection })));
      const expectedCollection: IAPGIPlante[] = [apg1, ...apg1Collection];
      jest.spyOn(aPGIPlanteService, 'addAPGIPlanteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      expect(aPGIPlanteService.query).toHaveBeenCalled();
      expect(aPGIPlanteService.addAPGIPlanteToCollectionIfMissing).toHaveBeenCalledWith(apg1Collection, apg1);
      expect(comp.apg1sCollection).toEqual(expectedCollection);
    });

    it('Should call apg2 query and add missing value', () => {
      const classification: IClassification = { id: 456 };
      const apg2: IAPGIIPlante = { id: 30924 };
      classification.apg2 = apg2;

      const apg2Collection: IAPGIIPlante[] = [{ id: 62577 }];
      jest.spyOn(aPGIIPlanteService, 'query').mockReturnValue(of(new HttpResponse({ body: apg2Collection })));
      const expectedCollection: IAPGIIPlante[] = [apg2, ...apg2Collection];
      jest.spyOn(aPGIIPlanteService, 'addAPGIIPlanteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      expect(aPGIIPlanteService.query).toHaveBeenCalled();
      expect(aPGIIPlanteService.addAPGIIPlanteToCollectionIfMissing).toHaveBeenCalledWith(apg2Collection, apg2);
      expect(comp.apg2sCollection).toEqual(expectedCollection);
    });

    it('Should call apg3 query and add missing value', () => {
      const classification: IClassification = { id: 456 };
      const apg3: IAPGIIIPlante = { id: 93105 };
      classification.apg3 = apg3;

      const apg3Collection: IAPGIIIPlante[] = [{ id: 65331 }];
      jest.spyOn(aPGIIIPlanteService, 'query').mockReturnValue(of(new HttpResponse({ body: apg3Collection })));
      const expectedCollection: IAPGIIIPlante[] = [apg3, ...apg3Collection];
      jest.spyOn(aPGIIIPlanteService, 'addAPGIIIPlanteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      expect(aPGIIIPlanteService.query).toHaveBeenCalled();
      expect(aPGIIIPlanteService.addAPGIIIPlanteToCollectionIfMissing).toHaveBeenCalledWith(apg3Collection, apg3);
      expect(comp.apg3sCollection).toEqual(expectedCollection);
    });

    it('Should call apg4 query and add missing value', () => {
      const classification: IClassification = { id: 456 };
      const apg4: IAPGIVPlante = { id: 41475 };
      classification.apg4 = apg4;

      const apg4Collection: IAPGIVPlante[] = [{ id: 40404 }];
      jest.spyOn(aPGIVPlanteService, 'query').mockReturnValue(of(new HttpResponse({ body: apg4Collection })));
      const expectedCollection: IAPGIVPlante[] = [apg4, ...apg4Collection];
      jest.spyOn(aPGIVPlanteService, 'addAPGIVPlanteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      expect(aPGIVPlanteService.query).toHaveBeenCalled();
      expect(aPGIVPlanteService.addAPGIVPlanteToCollectionIfMissing).toHaveBeenCalledWith(apg4Collection, apg4);
      expect(comp.apg4sCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const classification: IClassification = { id: 456 };
      const raunkier: IRaunkierPlante = { id: 46444 };
      classification.raunkier = raunkier;
      const cronquist: ICronquistPlante = { id: 9164 };
      classification.cronquist = cronquist;
      const apg1: IAPGIPlante = { id: 89765 };
      classification.apg1 = apg1;
      const apg2: IAPGIIPlante = { id: 72072 };
      classification.apg2 = apg2;
      const apg3: IAPGIIIPlante = { id: 6641 };
      classification.apg3 = apg3;
      const apg4: IAPGIVPlante = { id: 49151 };
      classification.apg4 = apg4;

      activatedRoute.data = of({ classification });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(classification));
      expect(comp.raunkiersCollection).toContain(raunkier);
      expect(comp.cronquistsCollection).toContain(cronquist);
      expect(comp.apg1sCollection).toContain(apg1);
      expect(comp.apg2sCollection).toContain(apg2);
      expect(comp.apg3sCollection).toContain(apg3);
      expect(comp.apg4sCollection).toContain(apg4);
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
    describe('trackRaunkierPlanteById', () => {
      it('Should return tracked RaunkierPlante primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRaunkierPlanteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCronquistPlanteById', () => {
      it('Should return tracked CronquistPlante primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCronquistPlanteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAPGIPlanteById', () => {
      it('Should return tracked APGIPlante primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAPGIPlanteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAPGIIPlanteById', () => {
      it('Should return tracked APGIIPlante primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAPGIIPlanteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAPGIIIPlanteById', () => {
      it('Should return tracked APGIIIPlante primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAPGIIIPlanteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAPGIVPlanteById', () => {
      it('Should return tracked APGIVPlante primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAPGIVPlanteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
