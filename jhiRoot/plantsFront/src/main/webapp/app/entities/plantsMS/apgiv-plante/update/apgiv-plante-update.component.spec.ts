jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { APGIVPlanteService } from '../service/apgiv-plante.service';
import { IAPGIVPlante, APGIVPlante } from '../apgiv-plante.model';

import { APGIVPlanteUpdateComponent } from './apgiv-plante-update.component';

describe('APGIVPlante Management Update Component', () => {
  let comp: APGIVPlanteUpdateComponent;
  let fixture: ComponentFixture<APGIVPlanteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aPGIVPlanteService: APGIVPlanteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [APGIVPlanteUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(APGIVPlanteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(APGIVPlanteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aPGIVPlanteService = TestBed.inject(APGIVPlanteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const aPGIVPlante: IAPGIVPlante = { id: 456 };

      activatedRoute.data = of({ aPGIVPlante });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(aPGIVPlante));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGIVPlante>>();
      const aPGIVPlante = { id: 123 };
      jest.spyOn(aPGIVPlanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGIVPlante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aPGIVPlante }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(aPGIVPlanteService.update).toHaveBeenCalledWith(aPGIVPlante);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGIVPlante>>();
      const aPGIVPlante = new APGIVPlante();
      jest.spyOn(aPGIVPlanteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGIVPlante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aPGIVPlante }));
      saveSubject.complete();

      // THEN
      expect(aPGIVPlanteService.create).toHaveBeenCalledWith(aPGIVPlante);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGIVPlante>>();
      const aPGIVPlante = { id: 123 };
      jest.spyOn(aPGIVPlanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGIVPlante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aPGIVPlanteService.update).toHaveBeenCalledWith(aPGIVPlante);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
