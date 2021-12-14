jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SuperRegneService } from '../service/super-regne.service';
import { ISuperRegne, SuperRegne } from '../super-regne.model';

import { SuperRegneUpdateComponent } from './super-regne-update.component';

describe('SuperRegne Management Update Component', () => {
  let comp: SuperRegneUpdateComponent;
  let fixture: ComponentFixture<SuperRegneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let superRegneService: SuperRegneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SuperRegneUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SuperRegneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SuperRegneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    superRegneService = TestBed.inject(SuperRegneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SuperRegne query and add missing value', () => {
      const superRegne: ISuperRegne = { id: 456 };
      const superRegne: ISuperRegne = { id: 23 };
      superRegne.superRegne = superRegne;

      const superRegneCollection: ISuperRegne[] = [{ id: 42917 }];
      jest.spyOn(superRegneService, 'query').mockReturnValue(of(new HttpResponse({ body: superRegneCollection })));
      const additionalSuperRegnes = [superRegne];
      const expectedCollection: ISuperRegne[] = [...additionalSuperRegnes, ...superRegneCollection];
      jest.spyOn(superRegneService, 'addSuperRegneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ superRegne });
      comp.ngOnInit();

      expect(superRegneService.query).toHaveBeenCalled();
      expect(superRegneService.addSuperRegneToCollectionIfMissing).toHaveBeenCalledWith(superRegneCollection, ...additionalSuperRegnes);
      expect(comp.superRegnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const superRegne: ISuperRegne = { id: 456 };
      const superRegne: ISuperRegne = { id: 59986 };
      superRegne.superRegne = superRegne;

      activatedRoute.data = of({ superRegne });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(superRegne));
      expect(comp.superRegnesSharedCollection).toContain(superRegne);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SuperRegne>>();
      const superRegne = { id: 123 };
      jest.spyOn(superRegneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ superRegne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: superRegne }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(superRegneService.update).toHaveBeenCalledWith(superRegne);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SuperRegne>>();
      const superRegne = new SuperRegne();
      jest.spyOn(superRegneService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ superRegne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: superRegne }));
      saveSubject.complete();

      // THEN
      expect(superRegneService.create).toHaveBeenCalledWith(superRegne);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SuperRegne>>();
      const superRegne = { id: 123 };
      jest.spyOn(superRegneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ superRegne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(superRegneService.update).toHaveBeenCalledWith(superRegne);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSuperRegneById', () => {
      it('Should return tracked SuperRegne primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSuperRegneById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
