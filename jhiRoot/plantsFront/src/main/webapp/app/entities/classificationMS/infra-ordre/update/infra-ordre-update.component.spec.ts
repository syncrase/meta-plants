jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InfraOrdreService } from '../service/infra-ordre.service';
import { IInfraOrdre, InfraOrdre } from '../infra-ordre.model';
import { ISousOrdre } from 'app/entities/classificationMS/sous-ordre/sous-ordre.model';
import { SousOrdreService } from 'app/entities/classificationMS/sous-ordre/service/sous-ordre.service';

import { InfraOrdreUpdateComponent } from './infra-ordre-update.component';

describe('InfraOrdre Management Update Component', () => {
  let comp: InfraOrdreUpdateComponent;
  let fixture: ComponentFixture<InfraOrdreUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let infraOrdreService: InfraOrdreService;
  let sousOrdreService: SousOrdreService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [InfraOrdreUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(InfraOrdreUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InfraOrdreUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    infraOrdreService = TestBed.inject(InfraOrdreService);
    sousOrdreService = TestBed.inject(SousOrdreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InfraOrdre query and add missing value', () => {
      const infraOrdre: IInfraOrdre = { id: 456 };
      const infraOrdre: IInfraOrdre = { id: 78334 };
      infraOrdre.infraOrdre = infraOrdre;

      const infraOrdreCollection: IInfraOrdre[] = [{ id: 97890 }];
      jest.spyOn(infraOrdreService, 'query').mockReturnValue(of(new HttpResponse({ body: infraOrdreCollection })));
      const additionalInfraOrdres = [infraOrdre];
      const expectedCollection: IInfraOrdre[] = [...additionalInfraOrdres, ...infraOrdreCollection];
      jest.spyOn(infraOrdreService, 'addInfraOrdreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ infraOrdre });
      comp.ngOnInit();

      expect(infraOrdreService.query).toHaveBeenCalled();
      expect(infraOrdreService.addInfraOrdreToCollectionIfMissing).toHaveBeenCalledWith(infraOrdreCollection, ...additionalInfraOrdres);
      expect(comp.infraOrdresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SousOrdre query and add missing value', () => {
      const infraOrdre: IInfraOrdre = { id: 456 };
      const sousOrdre: ISousOrdre = { id: 98199 };
      infraOrdre.sousOrdre = sousOrdre;

      const sousOrdreCollection: ISousOrdre[] = [{ id: 43955 }];
      jest.spyOn(sousOrdreService, 'query').mockReturnValue(of(new HttpResponse({ body: sousOrdreCollection })));
      const additionalSousOrdres = [sousOrdre];
      const expectedCollection: ISousOrdre[] = [...additionalSousOrdres, ...sousOrdreCollection];
      jest.spyOn(sousOrdreService, 'addSousOrdreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ infraOrdre });
      comp.ngOnInit();

      expect(sousOrdreService.query).toHaveBeenCalled();
      expect(sousOrdreService.addSousOrdreToCollectionIfMissing).toHaveBeenCalledWith(sousOrdreCollection, ...additionalSousOrdres);
      expect(comp.sousOrdresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const infraOrdre: IInfraOrdre = { id: 456 };
      const infraOrdre: IInfraOrdre = { id: 34837 };
      infraOrdre.infraOrdre = infraOrdre;
      const sousOrdre: ISousOrdre = { id: 23888 };
      infraOrdre.sousOrdre = sousOrdre;

      activatedRoute.data = of({ infraOrdre });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(infraOrdre));
      expect(comp.infraOrdresSharedCollection).toContain(infraOrdre);
      expect(comp.sousOrdresSharedCollection).toContain(sousOrdre);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InfraOrdre>>();
      const infraOrdre = { id: 123 };
      jest.spyOn(infraOrdreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infraOrdre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: infraOrdre }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(infraOrdreService.update).toHaveBeenCalledWith(infraOrdre);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InfraOrdre>>();
      const infraOrdre = new InfraOrdre();
      jest.spyOn(infraOrdreService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infraOrdre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: infraOrdre }));
      saveSubject.complete();

      // THEN
      expect(infraOrdreService.create).toHaveBeenCalledWith(infraOrdre);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InfraOrdre>>();
      const infraOrdre = { id: 123 };
      jest.spyOn(infraOrdreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infraOrdre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(infraOrdreService.update).toHaveBeenCalledWith(infraOrdre);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackInfraOrdreById', () => {
      it('Should return tracked InfraOrdre primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInfraOrdreById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSousOrdreById', () => {
      it('Should return tracked SousOrdre primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousOrdreById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
