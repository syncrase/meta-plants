jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RaunkierService } from '../service/raunkier.service';
import { IRaunkier, Raunkier } from '../raunkier.model';

import { RaunkierUpdateComponent } from './raunkier-update.component';

describe('Raunkier Management Update Component', () => {
  let comp: RaunkierUpdateComponent;
  let fixture: ComponentFixture<RaunkierUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let raunkierService: RaunkierService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RaunkierUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RaunkierUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RaunkierUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    raunkierService = TestBed.inject(RaunkierService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const raunkier: IRaunkier = { id: 456 };

      activatedRoute.data = of({ raunkier });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(raunkier));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Raunkier>>();
      const raunkier = { id: 123 };
      jest.spyOn(raunkierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ raunkier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: raunkier }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(raunkierService.update).toHaveBeenCalledWith(raunkier);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Raunkier>>();
      const raunkier = new Raunkier();
      jest.spyOn(raunkierService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ raunkier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: raunkier }));
      saveSubject.complete();

      // THEN
      expect(raunkierService.create).toHaveBeenCalledWith(raunkier);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Raunkier>>();
      const raunkier = { id: 123 };
      jest.spyOn(raunkierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ raunkier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(raunkierService.update).toHaveBeenCalledWith(raunkier);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
