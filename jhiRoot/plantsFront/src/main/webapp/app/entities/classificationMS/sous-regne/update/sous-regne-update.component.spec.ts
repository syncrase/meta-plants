jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SousRegneService } from '../service/sous-regne.service';
import { ISousRegne, SousRegne } from '../sous-regne.model';
import { IRegne } from 'app/entities/classificationMS/regne/regne.model';
import { RegneService } from 'app/entities/classificationMS/regne/service/regne.service';

import { SousRegneUpdateComponent } from './sous-regne-update.component';

describe('SousRegne Management Update Component', () => {
  let comp: SousRegneUpdateComponent;
  let fixture: ComponentFixture<SousRegneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sousRegneService: SousRegneService;
  let regneService: RegneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SousRegneUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SousRegneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SousRegneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sousRegneService = TestBed.inject(SousRegneService);
    regneService = TestBed.inject(RegneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SousRegne query and add missing value', () => {
      const sousRegne: ISousRegne = { id: 456 };
      const sousRegne: ISousRegne = { id: 80195 };
      sousRegne.sousRegne = sousRegne;

      const sousRegneCollection: ISousRegne[] = [{ id: 14994 }];
      jest.spyOn(sousRegneService, 'query').mockReturnValue(of(new HttpResponse({ body: sousRegneCollection })));
      const additionalSousRegnes = [sousRegne];
      const expectedCollection: ISousRegne[] = [...additionalSousRegnes, ...sousRegneCollection];
      jest.spyOn(sousRegneService, 'addSousRegneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousRegne });
      comp.ngOnInit();

      expect(sousRegneService.query).toHaveBeenCalled();
      expect(sousRegneService.addSousRegneToCollectionIfMissing).toHaveBeenCalledWith(sousRegneCollection, ...additionalSousRegnes);
      expect(comp.sousRegnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Regne query and add missing value', () => {
      const sousRegne: ISousRegne = { id: 456 };
      const regne: IRegne = { id: 45707 };
      sousRegne.regne = regne;

      const regneCollection: IRegne[] = [{ id: 37845 }];
      jest.spyOn(regneService, 'query').mockReturnValue(of(new HttpResponse({ body: regneCollection })));
      const additionalRegnes = [regne];
      const expectedCollection: IRegne[] = [...additionalRegnes, ...regneCollection];
      jest.spyOn(regneService, 'addRegneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousRegne });
      comp.ngOnInit();

      expect(regneService.query).toHaveBeenCalled();
      expect(regneService.addRegneToCollectionIfMissing).toHaveBeenCalledWith(regneCollection, ...additionalRegnes);
      expect(comp.regnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sousRegne: ISousRegne = { id: 456 };
      const sousRegne: ISousRegne = { id: 2306 };
      sousRegne.sousRegne = sousRegne;
      const regne: IRegne = { id: 90365 };
      sousRegne.regne = regne;

      activatedRoute.data = of({ sousRegne });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sousRegne));
      expect(comp.sousRegnesSharedCollection).toContain(sousRegne);
      expect(comp.regnesSharedCollection).toContain(regne);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousRegne>>();
      const sousRegne = { id: 123 };
      jest.spyOn(sousRegneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousRegne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousRegne }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sousRegneService.update).toHaveBeenCalledWith(sousRegne);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousRegne>>();
      const sousRegne = new SousRegne();
      jest.spyOn(sousRegneService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousRegne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousRegne }));
      saveSubject.complete();

      // THEN
      expect(sousRegneService.create).toHaveBeenCalledWith(sousRegne);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousRegne>>();
      const sousRegne = { id: 123 };
      jest.spyOn(sousRegneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousRegne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sousRegneService.update).toHaveBeenCalledWith(sousRegne);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSousRegneById', () => {
      it('Should return tracked SousRegne primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousRegneById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackRegneById', () => {
      it('Should return tracked Regne primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRegneById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
