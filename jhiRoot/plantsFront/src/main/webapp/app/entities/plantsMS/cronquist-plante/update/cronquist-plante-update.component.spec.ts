jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CronquistPlanteService } from '../service/cronquist-plante.service';
import { ICronquistPlante, CronquistPlante } from '../cronquist-plante.model';

import { CronquistPlanteUpdateComponent } from './cronquist-plante-update.component';

describe('CronquistPlante Management Update Component', () => {
  let comp: CronquistPlanteUpdateComponent;
  let fixture: ComponentFixture<CronquistPlanteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cronquistPlanteService: CronquistPlanteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CronquistPlanteUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CronquistPlanteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CronquistPlanteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cronquistPlanteService = TestBed.inject(CronquistPlanteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cronquistPlante: ICronquistPlante = { id: 456 };

      activatedRoute.data = of({ cronquistPlante });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cronquistPlante));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CronquistPlante>>();
      const cronquistPlante = { id: 123 };
      jest.spyOn(cronquistPlanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cronquistPlante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cronquistPlante }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cronquistPlanteService.update).toHaveBeenCalledWith(cronquistPlante);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CronquistPlante>>();
      const cronquistPlante = new CronquistPlante();
      jest.spyOn(cronquistPlanteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cronquistPlante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cronquistPlante }));
      saveSubject.complete();

      // THEN
      expect(cronquistPlanteService.create).toHaveBeenCalledWith(cronquistPlante);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CronquistPlante>>();
      const cronquistPlante = { id: 123 };
      jest.spyOn(cronquistPlanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cronquistPlante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cronquistPlanteService.update).toHaveBeenCalledWith(cronquistPlante);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
