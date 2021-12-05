jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExpositionService } from '../service/exposition.service';
import { IExposition, Exposition } from '../exposition.model';
import { IPlante } from 'app/entities/microservice/plante/plante.model';
import { PlanteService } from 'app/entities/microservice/plante/service/plante.service';

import { ExpositionUpdateComponent } from './exposition-update.component';

describe('Exposition Management Update Component', () => {
  let comp: ExpositionUpdateComponent;
  let fixture: ComponentFixture<ExpositionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let expositionService: ExpositionService;
  let planteService: PlanteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ExpositionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ExpositionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ExpositionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    expositionService = TestBed.inject(ExpositionService);
    planteService = TestBed.inject(PlanteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Plante query and add missing value', () => {
      const exposition: IExposition = { id: 456 };
      const plante: IPlante = { id: 48625 };
      exposition.plante = plante;

      const planteCollection: IPlante[] = [{ id: 99742 }];
      jest.spyOn(planteService, 'query').mockReturnValue(of(new HttpResponse({ body: planteCollection })));
      const additionalPlantes = [plante];
      const expectedCollection: IPlante[] = [...additionalPlantes, ...planteCollection];
      jest.spyOn(planteService, 'addPlanteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ exposition });
      comp.ngOnInit();

      expect(planteService.query).toHaveBeenCalled();
      expect(planteService.addPlanteToCollectionIfMissing).toHaveBeenCalledWith(planteCollection, ...additionalPlantes);
      expect(comp.plantesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const exposition: IExposition = { id: 456 };
      const plante: IPlante = { id: 11016 };
      exposition.plante = plante;

      activatedRoute.data = of({ exposition });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(exposition));
      expect(comp.plantesSharedCollection).toContain(plante);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Exposition>>();
      const exposition = { id: 123 };
      jest.spyOn(expositionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exposition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exposition }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(expositionService.update).toHaveBeenCalledWith(exposition);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Exposition>>();
      const exposition = new Exposition();
      jest.spyOn(expositionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exposition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: exposition }));
      saveSubject.complete();

      // THEN
      expect(expositionService.create).toHaveBeenCalledWith(exposition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Exposition>>();
      const exposition = { id: 123 };
      jest.spyOn(expositionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ exposition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(expositionService.update).toHaveBeenCalledWith(exposition);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackPlanteById', () => {
      it('Should return tracked Plante primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPlanteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
