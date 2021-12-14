jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SousOrdreService } from '../service/sous-ordre.service';
import { ISousOrdre, SousOrdre } from '../sous-ordre.model';
import { IOrdre } from 'app/entities/classificationMS/ordre/ordre.model';
import { OrdreService } from 'app/entities/classificationMS/ordre/service/ordre.service';

import { SousOrdreUpdateComponent } from './sous-ordre-update.component';

describe('SousOrdre Management Update Component', () => {
  let comp: SousOrdreUpdateComponent;
  let fixture: ComponentFixture<SousOrdreUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sousOrdreService: SousOrdreService;
  let ordreService: OrdreService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SousOrdreUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SousOrdreUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SousOrdreUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sousOrdreService = TestBed.inject(SousOrdreService);
    ordreService = TestBed.inject(OrdreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SousOrdre query and add missing value', () => {
      const sousOrdre: ISousOrdre = { id: 456 };
      const sousOrdre: ISousOrdre = { id: 25987 };
      sousOrdre.sousOrdre = sousOrdre;

      const sousOrdreCollection: ISousOrdre[] = [{ id: 21373 }];
      jest.spyOn(sousOrdreService, 'query').mockReturnValue(of(new HttpResponse({ body: sousOrdreCollection })));
      const additionalSousOrdres = [sousOrdre];
      const expectedCollection: ISousOrdre[] = [...additionalSousOrdres, ...sousOrdreCollection];
      jest.spyOn(sousOrdreService, 'addSousOrdreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousOrdre });
      comp.ngOnInit();

      expect(sousOrdreService.query).toHaveBeenCalled();
      expect(sousOrdreService.addSousOrdreToCollectionIfMissing).toHaveBeenCalledWith(sousOrdreCollection, ...additionalSousOrdres);
      expect(comp.sousOrdresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Ordre query and add missing value', () => {
      const sousOrdre: ISousOrdre = { id: 456 };
      const ordre: IOrdre = { id: 28773 };
      sousOrdre.ordre = ordre;

      const ordreCollection: IOrdre[] = [{ id: 12541 }];
      jest.spyOn(ordreService, 'query').mockReturnValue(of(new HttpResponse({ body: ordreCollection })));
      const additionalOrdres = [ordre];
      const expectedCollection: IOrdre[] = [...additionalOrdres, ...ordreCollection];
      jest.spyOn(ordreService, 'addOrdreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousOrdre });
      comp.ngOnInit();

      expect(ordreService.query).toHaveBeenCalled();
      expect(ordreService.addOrdreToCollectionIfMissing).toHaveBeenCalledWith(ordreCollection, ...additionalOrdres);
      expect(comp.ordresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sousOrdre: ISousOrdre = { id: 456 };
      const sousOrdre: ISousOrdre = { id: 73878 };
      sousOrdre.sousOrdre = sousOrdre;
      const ordre: IOrdre = { id: 40314 };
      sousOrdre.ordre = ordre;

      activatedRoute.data = of({ sousOrdre });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sousOrdre));
      expect(comp.sousOrdresSharedCollection).toContain(sousOrdre);
      expect(comp.ordresSharedCollection).toContain(ordre);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousOrdre>>();
      const sousOrdre = { id: 123 };
      jest.spyOn(sousOrdreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousOrdre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousOrdre }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sousOrdreService.update).toHaveBeenCalledWith(sousOrdre);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousOrdre>>();
      const sousOrdre = new SousOrdre();
      jest.spyOn(sousOrdreService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousOrdre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousOrdre }));
      saveSubject.complete();

      // THEN
      expect(sousOrdreService.create).toHaveBeenCalledWith(sousOrdre);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousOrdre>>();
      const sousOrdre = { id: 123 };
      jest.spyOn(sousOrdreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousOrdre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sousOrdreService.update).toHaveBeenCalledWith(sousOrdre);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSousOrdreById', () => {
      it('Should return tracked SousOrdre primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousOrdreById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackOrdreById', () => {
      it('Should return tracked Ordre primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackOrdreById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
