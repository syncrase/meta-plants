jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrdreService } from '../service/ordre.service';
import { IOrdre, Ordre } from '../ordre.model';
import { ISuperOrdre } from 'app/entities/classificationMS/super-ordre/super-ordre.model';
import { SuperOrdreService } from 'app/entities/classificationMS/super-ordre/service/super-ordre.service';

import { OrdreUpdateComponent } from './ordre-update.component';

describe('Ordre Management Update Component', () => {
  let comp: OrdreUpdateComponent;
  let fixture: ComponentFixture<OrdreUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ordreService: OrdreService;
  let superOrdreService: SuperOrdreService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [OrdreUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(OrdreUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrdreUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ordreService = TestBed.inject(OrdreService);
    superOrdreService = TestBed.inject(SuperOrdreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Ordre query and add missing value', () => {
      const ordre: IOrdre = { id: 456 };
      const ordre: IOrdre = { id: 44321 };
      ordre.ordre = ordre;

      const ordreCollection: IOrdre[] = [{ id: 2928 }];
      jest.spyOn(ordreService, 'query').mockReturnValue(of(new HttpResponse({ body: ordreCollection })));
      const additionalOrdres = [ordre];
      const expectedCollection: IOrdre[] = [...additionalOrdres, ...ordreCollection];
      jest.spyOn(ordreService, 'addOrdreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ordre });
      comp.ngOnInit();

      expect(ordreService.query).toHaveBeenCalled();
      expect(ordreService.addOrdreToCollectionIfMissing).toHaveBeenCalledWith(ordreCollection, ...additionalOrdres);
      expect(comp.ordresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SuperOrdre query and add missing value', () => {
      const ordre: IOrdre = { id: 456 };
      const superOrdre: ISuperOrdre = { id: 97226 };
      ordre.superOrdre = superOrdre;

      const superOrdreCollection: ISuperOrdre[] = [{ id: 74577 }];
      jest.spyOn(superOrdreService, 'query').mockReturnValue(of(new HttpResponse({ body: superOrdreCollection })));
      const additionalSuperOrdres = [superOrdre];
      const expectedCollection: ISuperOrdre[] = [...additionalSuperOrdres, ...superOrdreCollection];
      jest.spyOn(superOrdreService, 'addSuperOrdreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ordre });
      comp.ngOnInit();

      expect(superOrdreService.query).toHaveBeenCalled();
      expect(superOrdreService.addSuperOrdreToCollectionIfMissing).toHaveBeenCalledWith(superOrdreCollection, ...additionalSuperOrdres);
      expect(comp.superOrdresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ordre: IOrdre = { id: 456 };
      const ordre: IOrdre = { id: 57629 };
      ordre.ordre = ordre;
      const superOrdre: ISuperOrdre = { id: 75386 };
      ordre.superOrdre = superOrdre;

      activatedRoute.data = of({ ordre });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(ordre));
      expect(comp.ordresSharedCollection).toContain(ordre);
      expect(comp.superOrdresSharedCollection).toContain(superOrdre);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ordre>>();
      const ordre = { id: 123 };
      jest.spyOn(ordreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ordre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ordre }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(ordreService.update).toHaveBeenCalledWith(ordre);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ordre>>();
      const ordre = new Ordre();
      jest.spyOn(ordreService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ordre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ordre }));
      saveSubject.complete();

      // THEN
      expect(ordreService.create).toHaveBeenCalledWith(ordre);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Ordre>>();
      const ordre = { id: 123 };
      jest.spyOn(ordreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ordre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ordreService.update).toHaveBeenCalledWith(ordre);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackOrdreById', () => {
      it('Should return tracked Ordre primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOrdreById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSuperOrdreById', () => {
      it('Should return tracked SuperOrdre primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSuperOrdreById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
