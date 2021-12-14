jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RameauService } from '../service/rameau.service';
import { IRameau, Rameau } from '../rameau.model';
import { ISousRegne } from 'app/entities/classificationMS/sous-regne/sous-regne.model';
import { SousRegneService } from 'app/entities/classificationMS/sous-regne/service/sous-regne.service';

import { RameauUpdateComponent } from './rameau-update.component';

describe('Rameau Management Update Component', () => {
  let comp: RameauUpdateComponent;
  let fixture: ComponentFixture<RameauUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let rameauService: RameauService;
  let sousRegneService: SousRegneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RameauUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(RameauUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RameauUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    rameauService = TestBed.inject(RameauService);
    sousRegneService = TestBed.inject(SousRegneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Rameau query and add missing value', () => {
      const rameau: IRameau = { id: 456 };
      const rameau: IRameau = { id: 29395 };
      rameau.rameau = rameau;

      const rameauCollection: IRameau[] = [{ id: 79924 }];
      jest.spyOn(rameauService, 'query').mockReturnValue(of(new HttpResponse({ body: rameauCollection })));
      const additionalRameaus = [rameau];
      const expectedCollection: IRameau[] = [...additionalRameaus, ...rameauCollection];
      jest.spyOn(rameauService, 'addRameauToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rameau });
      comp.ngOnInit();

      expect(rameauService.query).toHaveBeenCalled();
      expect(rameauService.addRameauToCollectionIfMissing).toHaveBeenCalledWith(rameauCollection, ...additionalRameaus);
      expect(comp.rameausSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SousRegne query and add missing value', () => {
      const rameau: IRameau = { id: 456 };
      const sousRegne: ISousRegne = { id: 32192 };
      rameau.sousRegne = sousRegne;

      const sousRegneCollection: ISousRegne[] = [{ id: 64844 }];
      jest.spyOn(sousRegneService, 'query').mockReturnValue(of(new HttpResponse({ body: sousRegneCollection })));
      const additionalSousRegnes = [sousRegne];
      const expectedCollection: ISousRegne[] = [...additionalSousRegnes, ...sousRegneCollection];
      jest.spyOn(sousRegneService, 'addSousRegneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ rameau });
      comp.ngOnInit();

      expect(sousRegneService.query).toHaveBeenCalled();
      expect(sousRegneService.addSousRegneToCollectionIfMissing).toHaveBeenCalledWith(sousRegneCollection, ...additionalSousRegnes);
      expect(comp.sousRegnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const rameau: IRameau = { id: 456 };
      const rameau: IRameau = { id: 38304 };
      rameau.rameau = rameau;
      const sousRegne: ISousRegne = { id: 6567 };
      rameau.sousRegne = sousRegne;

      activatedRoute.data = of({ rameau });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(rameau));
      expect(comp.rameausSharedCollection).toContain(rameau);
      expect(comp.sousRegnesSharedCollection).toContain(sousRegne);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Rameau>>();
      const rameau = { id: 123 };
      jest.spyOn(rameauService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rameau });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rameau }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(rameauService.update).toHaveBeenCalledWith(rameau);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Rameau>>();
      const rameau = new Rameau();
      jest.spyOn(rameauService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rameau });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: rameau }));
      saveSubject.complete();

      // THEN
      expect(rameauService.create).toHaveBeenCalledWith(rameau);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Rameau>>();
      const rameau = { id: 123 };
      jest.spyOn(rameauService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ rameau });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(rameauService.update).toHaveBeenCalledWith(rameau);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackRameauById', () => {
      it('Should return tracked Rameau primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRameauById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSousRegneById', () => {
      it('Should return tracked SousRegne primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousRegneById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
