jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VarieteService } from '../service/variete.service';
import { IVariete, Variete } from '../variete.model';
import { ISousEspece } from 'app/entities/classificationMS/sous-espece/sous-espece.model';
import { SousEspeceService } from 'app/entities/classificationMS/sous-espece/service/sous-espece.service';

import { VarieteUpdateComponent } from './variete-update.component';

describe('Variete Management Update Component', () => {
  let comp: VarieteUpdateComponent;
  let fixture: ComponentFixture<VarieteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let varieteService: VarieteService;
  let sousEspeceService: SousEspeceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [VarieteUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(VarieteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VarieteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    varieteService = TestBed.inject(VarieteService);
    sousEspeceService = TestBed.inject(SousEspeceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Variete query and add missing value', () => {
      const variete: IVariete = { id: 456 };
      const variete: IVariete = { id: 72890 };
      variete.variete = variete;

      const varieteCollection: IVariete[] = [{ id: 53043 }];
      jest.spyOn(varieteService, 'query').mockReturnValue(of(new HttpResponse({ body: varieteCollection })));
      const additionalVarietes = [variete];
      const expectedCollection: IVariete[] = [...additionalVarietes, ...varieteCollection];
      jest.spyOn(varieteService, 'addVarieteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ variete });
      comp.ngOnInit();

      expect(varieteService.query).toHaveBeenCalled();
      expect(varieteService.addVarieteToCollectionIfMissing).toHaveBeenCalledWith(varieteCollection, ...additionalVarietes);
      expect(comp.varietesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SousEspece query and add missing value', () => {
      const variete: IVariete = { id: 456 };
      const sousEspece: ISousEspece = { id: 62382 };
      variete.sousEspece = sousEspece;

      const sousEspeceCollection: ISousEspece[] = [{ id: 66672 }];
      jest.spyOn(sousEspeceService, 'query').mockReturnValue(of(new HttpResponse({ body: sousEspeceCollection })));
      const additionalSousEspeces = [sousEspece];
      const expectedCollection: ISousEspece[] = [...additionalSousEspeces, ...sousEspeceCollection];
      jest.spyOn(sousEspeceService, 'addSousEspeceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ variete });
      comp.ngOnInit();

      expect(sousEspeceService.query).toHaveBeenCalled();
      expect(sousEspeceService.addSousEspeceToCollectionIfMissing).toHaveBeenCalledWith(sousEspeceCollection, ...additionalSousEspeces);
      expect(comp.sousEspecesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const variete: IVariete = { id: 456 };
      const variete: IVariete = { id: 90856 };
      variete.variete = variete;
      const sousEspece: ISousEspece = { id: 3626 };
      variete.sousEspece = sousEspece;

      activatedRoute.data = of({ variete });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(variete));
      expect(comp.varietesSharedCollection).toContain(variete);
      expect(comp.sousEspecesSharedCollection).toContain(sousEspece);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Variete>>();
      const variete = { id: 123 };
      jest.spyOn(varieteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ variete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: variete }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(varieteService.update).toHaveBeenCalledWith(variete);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Variete>>();
      const variete = new Variete();
      jest.spyOn(varieteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ variete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: variete }));
      saveSubject.complete();

      // THEN
      expect(varieteService.create).toHaveBeenCalledWith(variete);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Variete>>();
      const variete = { id: 123 };
      jest.spyOn(varieteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ variete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(varieteService.update).toHaveBeenCalledWith(variete);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackVarieteById', () => {
      it('Should return tracked Variete primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackVarieteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSousEspeceById', () => {
      it('Should return tracked SousEspece primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousEspeceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
