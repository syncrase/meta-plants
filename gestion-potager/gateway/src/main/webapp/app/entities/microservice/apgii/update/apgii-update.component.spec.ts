jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { APGIIService } from '../service/apgii.service';
import { IAPGII, APGII } from '../apgii.model';

import { APGIIUpdateComponent } from './apgii-update.component';

describe('APGII Management Update Component', () => {
  let comp: APGIIUpdateComponent;
  let fixture: ComponentFixture<APGIIUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aPGIIService: APGIIService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [APGIIUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(APGIIUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(APGIIUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aPGIIService = TestBed.inject(APGIIService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const aPGII: IAPGII = { id: 456 };

      activatedRoute.data = of({ aPGII });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(aPGII));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGII>>();
      const aPGII = { id: 123 };
      jest.spyOn(aPGIIService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGII });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aPGII }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(aPGIIService.update).toHaveBeenCalledWith(aPGII);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGII>>();
      const aPGII = new APGII();
      jest.spyOn(aPGIIService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGII });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aPGII }));
      saveSubject.complete();

      // THEN
      expect(aPGIIService.create).toHaveBeenCalledWith(aPGII);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGII>>();
      const aPGII = { id: 123 };
      jest.spyOn(aPGIIService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGII });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aPGIIService.update).toHaveBeenCalledWith(aPGII);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
