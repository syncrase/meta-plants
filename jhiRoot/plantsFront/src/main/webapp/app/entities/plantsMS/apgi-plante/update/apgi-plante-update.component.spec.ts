jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { APGIPlanteService } from '../service/apgi-plante.service';
import { IAPGIPlante, APGIPlante } from '../apgi-plante.model';

import { APGIPlanteUpdateComponent } from './apgi-plante-update.component';

describe('APGIPlante Management Update Component', () => {
  let comp: APGIPlanteUpdateComponent;
  let fixture: ComponentFixture<APGIPlanteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aPGIPlanteService: APGIPlanteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [APGIPlanteUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(APGIPlanteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(APGIPlanteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aPGIPlanteService = TestBed.inject(APGIPlanteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const aPGIPlante: IAPGIPlante = { id: 456 };

      activatedRoute.data = of({ aPGIPlante });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(aPGIPlante));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGIPlante>>();
      const aPGIPlante = { id: 123 };
      jest.spyOn(aPGIPlanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGIPlante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aPGIPlante }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(aPGIPlanteService.update).toHaveBeenCalledWith(aPGIPlante);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGIPlante>>();
      const aPGIPlante = new APGIPlante();
      jest.spyOn(aPGIPlanteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGIPlante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aPGIPlante }));
      saveSubject.complete();

      // THEN
      expect(aPGIPlanteService.create).toHaveBeenCalledWith(aPGIPlante);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGIPlante>>();
      const aPGIPlante = { id: 123 };
      jest.spyOn(aPGIPlanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGIPlante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aPGIPlanteService.update).toHaveBeenCalledWith(aPGIPlante);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
