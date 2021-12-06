jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SolService } from '../service/sol.service';
import { ISol, Sol } from '../sol.model';
import { IPlante } from 'app/entities/plante/plante.model';
import { PlanteService } from 'app/entities/plante/service/plante.service';

import { SolUpdateComponent } from './sol-update.component';

describe('Sol Management Update Component', () => {
  let comp: SolUpdateComponent;
  let fixture: ComponentFixture<SolUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let solService: SolService;
  let planteService: PlanteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SolUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SolUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SolUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    solService = TestBed.inject(SolService);
    planteService = TestBed.inject(PlanteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Plante query and add missing value', () => {
      const sol: ISol = { id: 456 };
      const plante: IPlante = { id: 5155 };
      sol.plante = plante;

      const planteCollection: IPlante[] = [{ id: 74635 }];
      jest.spyOn(planteService, 'query').mockReturnValue(of(new HttpResponse({ body: planteCollection })));
      const additionalPlantes = [plante];
      const expectedCollection: IPlante[] = [...additionalPlantes, ...planteCollection];
      jest.spyOn(planteService, 'addPlanteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sol });
      comp.ngOnInit();

      expect(planteService.query).toHaveBeenCalled();
      expect(planteService.addPlanteToCollectionIfMissing).toHaveBeenCalledWith(planteCollection, ...additionalPlantes);
      expect(comp.plantesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sol: ISol = { id: 456 };
      const plante: IPlante = { id: 38392 };
      sol.plante = plante;

      activatedRoute.data = of({ sol });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sol));
      expect(comp.plantesSharedCollection).toContain(plante);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sol>>();
      const sol = { id: 123 };
      jest.spyOn(solService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sol });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sol }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(solService.update).toHaveBeenCalledWith(sol);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sol>>();
      const sol = new Sol();
      jest.spyOn(solService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sol });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sol }));
      saveSubject.complete();

      // THEN
      expect(solService.create).toHaveBeenCalledWith(sol);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Sol>>();
      const sol = { id: 123 };
      jest.spyOn(solService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sol });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(solService.update).toHaveBeenCalledWith(sol);
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
