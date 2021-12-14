jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SousVarieteService } from '../service/sous-variete.service';
import { ISousVariete, SousVariete } from '../sous-variete.model';
import { IVariete } from 'app/entities/classificationMS/variete/variete.model';
import { VarieteService } from 'app/entities/classificationMS/variete/service/variete.service';

import { SousVarieteUpdateComponent } from './sous-variete-update.component';

describe('SousVariete Management Update Component', () => {
  let comp: SousVarieteUpdateComponent;
  let fixture: ComponentFixture<SousVarieteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sousVarieteService: SousVarieteService;
  let varieteService: VarieteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SousVarieteUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SousVarieteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SousVarieteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sousVarieteService = TestBed.inject(SousVarieteService);
    varieteService = TestBed.inject(VarieteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SousVariete query and add missing value', () => {
      const sousVariete: ISousVariete = { id: 456 };
      const sousVariete: ISousVariete = { id: 88865 };
      sousVariete.sousVariete = sousVariete;

      const sousVarieteCollection: ISousVariete[] = [{ id: 38462 }];
      jest.spyOn(sousVarieteService, 'query').mockReturnValue(of(new HttpResponse({ body: sousVarieteCollection })));
      const additionalSousVarietes = [sousVariete];
      const expectedCollection: ISousVariete[] = [...additionalSousVarietes, ...sousVarieteCollection];
      jest.spyOn(sousVarieteService, 'addSousVarieteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousVariete });
      comp.ngOnInit();

      expect(sousVarieteService.query).toHaveBeenCalled();
      expect(sousVarieteService.addSousVarieteToCollectionIfMissing).toHaveBeenCalledWith(sousVarieteCollection, ...additionalSousVarietes);
      expect(comp.sousVarietesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Variete query and add missing value', () => {
      const sousVariete: ISousVariete = { id: 456 };
      const variete: IVariete = { id: 17290 };
      sousVariete.variete = variete;

      const varieteCollection: IVariete[] = [{ id: 47942 }];
      jest.spyOn(varieteService, 'query').mockReturnValue(of(new HttpResponse({ body: varieteCollection })));
      const additionalVarietes = [variete];
      const expectedCollection: IVariete[] = [...additionalVarietes, ...varieteCollection];
      jest.spyOn(varieteService, 'addVarieteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousVariete });
      comp.ngOnInit();

      expect(varieteService.query).toHaveBeenCalled();
      expect(varieteService.addVarieteToCollectionIfMissing).toHaveBeenCalledWith(varieteCollection, ...additionalVarietes);
      expect(comp.varietesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sousVariete: ISousVariete = { id: 456 };
      const sousVariete: ISousVariete = { id: 62252 };
      sousVariete.sousVariete = sousVariete;
      const variete: IVariete = { id: 31111 };
      sousVariete.variete = variete;

      activatedRoute.data = of({ sousVariete });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sousVariete));
      expect(comp.sousVarietesSharedCollection).toContain(sousVariete);
      expect(comp.varietesSharedCollection).toContain(variete);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousVariete>>();
      const sousVariete = { id: 123 };
      jest.spyOn(sousVarieteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousVariete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousVariete }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sousVarieteService.update).toHaveBeenCalledWith(sousVariete);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousVariete>>();
      const sousVariete = new SousVariete();
      jest.spyOn(sousVarieteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousVariete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousVariete }));
      saveSubject.complete();

      // THEN
      expect(sousVarieteService.create).toHaveBeenCalledWith(sousVariete);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousVariete>>();
      const sousVariete = { id: 123 };
      jest.spyOn(sousVarieteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousVariete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sousVarieteService.update).toHaveBeenCalledWith(sousVariete);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSousVarieteById', () => {
      it('Should return tracked SousVariete primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousVarieteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackVarieteById', () => {
      it('Should return tracked Variete primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVarieteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
