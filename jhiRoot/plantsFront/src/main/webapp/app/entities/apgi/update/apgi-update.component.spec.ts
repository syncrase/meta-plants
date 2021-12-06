jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { APGIService } from '../service/apgi.service';
import { IAPGI, APGI } from '../apgi.model';

import { APGIUpdateComponent } from './apgi-update.component';

describe('APGI Management Update Component', () => {
  let comp: APGIUpdateComponent;
  let fixture: ComponentFixture<APGIUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aPGIService: APGIService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [APGIUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(APGIUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(APGIUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aPGIService = TestBed.inject(APGIService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const aPGI: IAPGI = { id: 456 };

      activatedRoute.data = of({ aPGI });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(aPGI));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGI>>();
      const aPGI = { id: 123 };
      jest.spyOn(aPGIService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGI });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aPGI }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(aPGIService.update).toHaveBeenCalledWith(aPGI);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGI>>();
      const aPGI = new APGI();
      jest.spyOn(aPGIService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGI });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aPGI }));
      saveSubject.complete();

      // THEN
      expect(aPGIService.create).toHaveBeenCalledWith(aPGI);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGI>>();
      const aPGI = { id: 123 };
      jest.spyOn(aPGIService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGI });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aPGIService.update).toHaveBeenCalledWith(aPGI);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
