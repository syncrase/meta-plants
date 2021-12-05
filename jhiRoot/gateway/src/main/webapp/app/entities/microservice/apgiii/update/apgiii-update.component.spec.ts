jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { APGIIIService } from '../service/apgiii.service';
import { IAPGIII, APGIII } from '../apgiii.model';

import { APGIIIUpdateComponent } from './apgiii-update.component';

describe('APGIII Management Update Component', () => {
  let comp: APGIIIUpdateComponent;
  let fixture: ComponentFixture<APGIIIUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aPGIIIService: APGIIIService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [APGIIIUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(APGIIIUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(APGIIIUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aPGIIIService = TestBed.inject(APGIIIService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const aPGIII: IAPGIII = { id: 456 };

      activatedRoute.data = of({ aPGIII });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(aPGIII));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGIII>>();
      const aPGIII = { id: 123 };
      jest.spyOn(aPGIIIService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGIII });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aPGIII }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(aPGIIIService.update).toHaveBeenCalledWith(aPGIII);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGIII>>();
      const aPGIII = new APGIII();
      jest.spyOn(aPGIIIService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGIII });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aPGIII }));
      saveSubject.complete();

      // THEN
      expect(aPGIIIService.create).toHaveBeenCalledWith(aPGIII);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGIII>>();
      const aPGIII = { id: 123 };
      jest.spyOn(aPGIIIService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGIII });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aPGIIIService.update).toHaveBeenCalledWith(aPGIII);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
