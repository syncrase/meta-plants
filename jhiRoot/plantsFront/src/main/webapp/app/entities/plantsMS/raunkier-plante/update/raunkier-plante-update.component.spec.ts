jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RaunkierPlanteService } from '../service/raunkier-plante.service';
import { IRaunkierPlante, RaunkierPlante } from '../raunkier-plante.model';

import { RaunkierPlanteUpdateComponent } from './raunkier-plante-update.component';

describe('RaunkierPlante Management Update Component', () => {
  let comp: RaunkierPlanteUpdateComponent;
  let fixture: ComponentFixture<RaunkierPlanteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let raunkierPlanteService: RaunkierPlanteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RaunkierPlanteUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RaunkierPlanteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RaunkierPlanteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    raunkierPlanteService = TestBed.inject(RaunkierPlanteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const raunkierPlante: IRaunkierPlante = { id: 456 };

      activatedRoute.data = of({ raunkierPlante });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(raunkierPlante));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RaunkierPlante>>();
      const raunkierPlante = { id: 123 };
      jest.spyOn(raunkierPlanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ raunkierPlante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: raunkierPlante }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(raunkierPlanteService.update).toHaveBeenCalledWith(raunkierPlante);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RaunkierPlante>>();
      const raunkierPlante = new RaunkierPlante();
      jest.spyOn(raunkierPlanteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ raunkierPlante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: raunkierPlante }));
      saveSubject.complete();

      // THEN
      expect(raunkierPlanteService.create).toHaveBeenCalledWith(raunkierPlante);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RaunkierPlante>>();
      const raunkierPlante = { id: 123 };
      jest.spyOn(raunkierPlanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ raunkierPlante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(raunkierPlanteService.update).toHaveBeenCalledWith(raunkierPlante);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
