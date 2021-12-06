jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { APGIVService } from '../service/apgiv.service';
import { IAPGIV, APGIV } from '../apgiv.model';

import { APGIVUpdateComponent } from './apgiv-update.component';

describe('APGIV Management Update Component', () => {
  let comp: APGIVUpdateComponent;
  let fixture: ComponentFixture<APGIVUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aPGIVService: APGIVService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [APGIVUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(APGIVUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(APGIVUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aPGIVService = TestBed.inject(APGIVService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const aPGIV: IAPGIV = { id: 456 };

      activatedRoute.data = of({ aPGIV });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(aPGIV));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGIV>>();
      const aPGIV = { id: 123 };
      jest.spyOn(aPGIVService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGIV });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aPGIV }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(aPGIVService.update).toHaveBeenCalledWith(aPGIV);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGIV>>();
      const aPGIV = new APGIV();
      jest.spyOn(aPGIVService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGIV });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aPGIV }));
      saveSubject.complete();

      // THEN
      expect(aPGIVService.create).toHaveBeenCalledWith(aPGIV);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGIV>>();
      const aPGIV = { id: 123 };
      jest.spyOn(aPGIVService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGIV });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aPGIVService.update).toHaveBeenCalledWith(aPGIV);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
