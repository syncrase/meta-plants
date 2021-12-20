jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClassificationCronquistService } from '../service/classification-cronquist.service';
import { IClassificationCronquist, ClassificationCronquist } from '../classification-cronquist.model';
import { IPlante } from 'app/entities/plantsMS/plante/plante.model';
import { PlanteService } from 'app/entities/plantsMS/plante/service/plante.service';

import { ClassificationCronquistUpdateComponent } from './classification-cronquist-update.component';

describe('ClassificationCronquist Management Update Component', () => {
  let comp: ClassificationCronquistUpdateComponent;
  let fixture: ComponentFixture<ClassificationCronquistUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let classificationCronquistService: ClassificationCronquistService;
  let planteService: PlanteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ClassificationCronquistUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ClassificationCronquistUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClassificationCronquistUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    classificationCronquistService = TestBed.inject(ClassificationCronquistService);
    planteService = TestBed.inject(PlanteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call plante query and add missing value', () => {
      const classificationCronquist: IClassificationCronquist = { id: 456 };
      const plante: IPlante = { id: 61110 };
      classificationCronquist.plante = plante;

      const planteCollection: IPlante[] = [{ id: 54382 }];
      jest.spyOn(planteService, 'query').mockReturnValue(of(new HttpResponse({ body: planteCollection })));
      const expectedCollection: IPlante[] = [plante, ...planteCollection];
      jest.spyOn(planteService, 'addPlanteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classificationCronquist });
      comp.ngOnInit();

      expect(planteService.query).toHaveBeenCalled();
      expect(planteService.addPlanteToCollectionIfMissing).toHaveBeenCalledWith(planteCollection, plante);
      expect(comp.plantesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const classificationCronquist: IClassificationCronquist = { id: 456 };
      const plante: IPlante = { id: 90607 };
      classificationCronquist.plante = plante;

      activatedRoute.data = of({ classificationCronquist });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(classificationCronquist));
      expect(comp.plantesCollection).toContain(plante);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ClassificationCronquist>>();
      const classificationCronquist = { id: 123 };
      jest.spyOn(classificationCronquistService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classificationCronquist });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classificationCronquist }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(classificationCronquistService.update).toHaveBeenCalledWith(classificationCronquist);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ClassificationCronquist>>();
      const classificationCronquist = new ClassificationCronquist();
      jest.spyOn(classificationCronquistService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classificationCronquist });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classificationCronquist }));
      saveSubject.complete();

      // THEN
      expect(classificationCronquistService.create).toHaveBeenCalledWith(classificationCronquist);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ClassificationCronquist>>();
      const classificationCronquist = { id: 123 };
      jest.spyOn(classificationCronquistService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classificationCronquist });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(classificationCronquistService.update).toHaveBeenCalledWith(classificationCronquist);
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
