jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RegneService } from '../service/regne.service';
import { IRegne, Regne } from '../regne.model';
import { ISuperRegne } from 'app/entities/classificationMS/super-regne/super-regne.model';
import { SuperRegneService } from 'app/entities/classificationMS/super-regne/service/super-regne.service';

import { RegneUpdateComponent } from './regne-update.component';

describe('Regne Management Update Component', () => {
  let comp: RegneUpdateComponent;
  let fixture: ComponentFixture<RegneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let regneService: RegneService;
  let superRegneService: SuperRegneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RegneUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RegneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RegneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    regneService = TestBed.inject(RegneService);
    superRegneService = TestBed.inject(SuperRegneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Regne query and add missing value', () => {
      const regne: IRegne = { id: 456 };
      const regne: IRegne = { id: 97485 };
      regne.regne = regne;

      const regneCollection: IRegne[] = [{ id: 42845 }];
      jest.spyOn(regneService, 'query').mockReturnValue(of(new HttpResponse({ body: regneCollection })));
      const additionalRegnes = [regne];
      const expectedCollection: IRegne[] = [...additionalRegnes, ...regneCollection];
      jest.spyOn(regneService, 'addRegneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ regne });
      comp.ngOnInit();

      expect(regneService.query).toHaveBeenCalled();
      expect(regneService.addRegneToCollectionIfMissing).toHaveBeenCalledWith(regneCollection, ...additionalRegnes);
      expect(comp.regnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SuperRegne query and add missing value', () => {
      const regne: IRegne = { id: 456 };
      const superRegne: ISuperRegne = { id: 47721 };
      regne.superRegne = superRegne;

      const superRegneCollection: ISuperRegne[] = [{ id: 67496 }];
      jest.spyOn(superRegneService, 'query').mockReturnValue(of(new HttpResponse({ body: superRegneCollection })));
      const additionalSuperRegnes = [superRegne];
      const expectedCollection: ISuperRegne[] = [...additionalSuperRegnes, ...superRegneCollection];
      jest.spyOn(superRegneService, 'addSuperRegneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ regne });
      comp.ngOnInit();

      expect(superRegneService.query).toHaveBeenCalled();
      expect(superRegneService.addSuperRegneToCollectionIfMissing).toHaveBeenCalledWith(superRegneCollection, ...additionalSuperRegnes);
      expect(comp.superRegnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const regne: IRegne = { id: 456 };
      const regne: IRegne = { id: 82207 };
      regne.regne = regne;
      const superRegne: ISuperRegne = { id: 22450 };
      regne.superRegne = superRegne;

      activatedRoute.data = of({ regne });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(regne));
      expect(comp.regnesSharedCollection).toContain(regne);
      expect(comp.superRegnesSharedCollection).toContain(superRegne);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Regne>>();
      const regne = { id: 123 };
      jest.spyOn(regneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ regne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: regne }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(regneService.update).toHaveBeenCalledWith(regne);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Regne>>();
      const regne = new Regne();
      jest.spyOn(regneService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ regne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: regne }));
      saveSubject.complete();

      // THEN
      expect(regneService.create).toHaveBeenCalledWith(regne);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Regne>>();
      const regne = { id: 123 };
      jest.spyOn(regneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ regne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(regneService.update).toHaveBeenCalledWith(regne);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackRegneById', () => {
      it('Should return tracked Regne primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRegneById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSuperRegneById', () => {
      it('Should return tracked SuperRegne primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSuperRegneById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
