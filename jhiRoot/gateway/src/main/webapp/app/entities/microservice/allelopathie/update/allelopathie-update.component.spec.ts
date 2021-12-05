jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AllelopathieService } from '../service/allelopathie.service';
import { IAllelopathie, Allelopathie } from '../allelopathie.model';
import { IPlante } from 'app/entities/microservice/plante/plante.model';
import { PlanteService } from 'app/entities/microservice/plante/service/plante.service';

import { AllelopathieUpdateComponent } from './allelopathie-update.component';

describe('Allelopathie Management Update Component', () => {
  let comp: AllelopathieUpdateComponent;
  let fixture: ComponentFixture<AllelopathieUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let allelopathieService: AllelopathieService;
  let planteService: PlanteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [AllelopathieUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(AllelopathieUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AllelopathieUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    allelopathieService = TestBed.inject(AllelopathieService);
    planteService = TestBed.inject(PlanteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Plante query and add missing value', () => {
      const allelopathie: IAllelopathie = { id: 456 };
      const interaction: IPlante = { id: 63418 };
      allelopathie.interaction = interaction;

      const planteCollection: IPlante[] = [{ id: 34714 }];
      jest.spyOn(planteService, 'query').mockReturnValue(of(new HttpResponse({ body: planteCollection })));
      const additionalPlantes = [interaction];
      const expectedCollection: IPlante[] = [...additionalPlantes, ...planteCollection];
      jest.spyOn(planteService, 'addPlanteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ allelopathie });
      comp.ngOnInit();

      expect(planteService.query).toHaveBeenCalled();
      expect(planteService.addPlanteToCollectionIfMissing).toHaveBeenCalledWith(planteCollection, ...additionalPlantes);
      expect(comp.plantesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call cible query and add missing value', () => {
      const allelopathie: IAllelopathie = { id: 456 };
      const cible: IPlante = { id: 79494 };
      allelopathie.cible = cible;

      const cibleCollection: IPlante[] = [{ id: 71666 }];
      jest.spyOn(planteService, 'query').mockReturnValue(of(new HttpResponse({ body: cibleCollection })));
      const expectedCollection: IPlante[] = [cible, ...cibleCollection];
      jest.spyOn(planteService, 'addPlanteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ allelopathie });
      comp.ngOnInit();

      expect(planteService.query).toHaveBeenCalled();
      expect(planteService.addPlanteToCollectionIfMissing).toHaveBeenCalledWith(cibleCollection, cible);
      expect(comp.ciblesCollection).toEqual(expectedCollection);
    });

    it('Should call origine query and add missing value', () => {
      const allelopathie: IAllelopathie = { id: 456 };
      const origine: IPlante = { id: 42435 };
      allelopathie.origine = origine;

      const origineCollection: IPlante[] = [{ id: 29660 }];
      jest.spyOn(planteService, 'query').mockReturnValue(of(new HttpResponse({ body: origineCollection })));
      const expectedCollection: IPlante[] = [origine, ...origineCollection];
      jest.spyOn(planteService, 'addPlanteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ allelopathie });
      comp.ngOnInit();

      expect(planteService.query).toHaveBeenCalled();
      expect(planteService.addPlanteToCollectionIfMissing).toHaveBeenCalledWith(origineCollection, origine);
      expect(comp.originesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const allelopathie: IAllelopathie = { id: 456 };
      const cible: IPlante = { id: 50857 };
      allelopathie.cible = cible;
      const origine: IPlante = { id: 32383 };
      allelopathie.origine = origine;
      const interaction: IPlante = { id: 87473 };
      allelopathie.interaction = interaction;

      activatedRoute.data = of({ allelopathie });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(allelopathie));
      expect(comp.ciblesCollection).toContain(cible);
      expect(comp.originesCollection).toContain(origine);
      expect(comp.plantesSharedCollection).toContain(interaction);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Allelopathie>>();
      const allelopathie = { id: 123 };
      jest.spyOn(allelopathieService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ allelopathie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: allelopathie }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(allelopathieService.update).toHaveBeenCalledWith(allelopathie);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Allelopathie>>();
      const allelopathie = new Allelopathie();
      jest.spyOn(allelopathieService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ allelopathie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: allelopathie }));
      saveSubject.complete();

      // THEN
      expect(allelopathieService.create).toHaveBeenCalledWith(allelopathie);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Allelopathie>>();
      const allelopathie = { id: 123 };
      jest.spyOn(allelopathieService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ allelopathie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(allelopathieService.update).toHaveBeenCalledWith(allelopathie);
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
