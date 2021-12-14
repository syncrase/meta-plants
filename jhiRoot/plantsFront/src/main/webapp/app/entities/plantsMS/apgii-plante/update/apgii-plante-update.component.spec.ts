jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { APGIIPlanteService } from '../service/apgii-plante.service';
import { IAPGIIPlante, APGIIPlante } from '../apgii-plante.model';

import { APGIIPlanteUpdateComponent } from './apgii-plante-update.component';

describe('APGIIPlante Management Update Component', () => {
  let comp: APGIIPlanteUpdateComponent;
  let fixture: ComponentFixture<APGIIPlanteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aPGIIPlanteService: APGIIPlanteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [APGIIPlanteUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(APGIIPlanteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(APGIIPlanteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aPGIIPlanteService = TestBed.inject(APGIIPlanteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const aPGIIPlante: IAPGIIPlante = { id: 456 };

      activatedRoute.data = of({ aPGIIPlante });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(aPGIIPlante));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGIIPlante>>();
      const aPGIIPlante = { id: 123 };
      jest.spyOn(aPGIIPlanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGIIPlante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aPGIIPlante }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(aPGIIPlanteService.update).toHaveBeenCalledWith(aPGIIPlante);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGIIPlante>>();
      const aPGIIPlante = new APGIIPlante();
      jest.spyOn(aPGIIPlanteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGIIPlante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aPGIIPlante }));
      saveSubject.complete();

      // THEN
      expect(aPGIIPlanteService.create).toHaveBeenCalledWith(aPGIIPlante);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGIIPlante>>();
      const aPGIIPlante = { id: 123 };
      jest.spyOn(aPGIIPlanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGIIPlante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aPGIIPlanteService.update).toHaveBeenCalledWith(aPGIIPlante);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
