jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SousFamilleService } from '../service/sous-famille.service';
import { ISousFamille, SousFamille } from '../sous-famille.model';
import { IFamille } from 'app/entities/classificationMS/famille/famille.model';
import { FamilleService } from 'app/entities/classificationMS/famille/service/famille.service';

import { SousFamilleUpdateComponent } from './sous-famille-update.component';

describe('SousFamille Management Update Component', () => {
  let comp: SousFamilleUpdateComponent;
  let fixture: ComponentFixture<SousFamilleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sousFamilleService: SousFamilleService;
  let familleService: FamilleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SousFamilleUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SousFamilleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SousFamilleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sousFamilleService = TestBed.inject(SousFamilleService);
    familleService = TestBed.inject(FamilleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SousFamille query and add missing value', () => {
      const sousFamille: ISousFamille = { id: 456 };
      const sousFamille: ISousFamille = { id: 77874 };
      sousFamille.sousFamille = sousFamille;

      const sousFamilleCollection: ISousFamille[] = [{ id: 7628 }];
      jest.spyOn(sousFamilleService, 'query').mockReturnValue(of(new HttpResponse({ body: sousFamilleCollection })));
      const additionalSousFamilles = [sousFamille];
      const expectedCollection: ISousFamille[] = [...additionalSousFamilles, ...sousFamilleCollection];
      jest.spyOn(sousFamilleService, 'addSousFamilleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousFamille });
      comp.ngOnInit();

      expect(sousFamilleService.query).toHaveBeenCalled();
      expect(sousFamilleService.addSousFamilleToCollectionIfMissing).toHaveBeenCalledWith(sousFamilleCollection, ...additionalSousFamilles);
      expect(comp.sousFamillesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Famille query and add missing value', () => {
      const sousFamille: ISousFamille = { id: 456 };
      const famille: IFamille = { id: 34645 };
      sousFamille.famille = famille;

      const familleCollection: IFamille[] = [{ id: 65730 }];
      jest.spyOn(familleService, 'query').mockReturnValue(of(new HttpResponse({ body: familleCollection })));
      const additionalFamilles = [famille];
      const expectedCollection: IFamille[] = [...additionalFamilles, ...familleCollection];
      jest.spyOn(familleService, 'addFamilleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousFamille });
      comp.ngOnInit();

      expect(familleService.query).toHaveBeenCalled();
      expect(familleService.addFamilleToCollectionIfMissing).toHaveBeenCalledWith(familleCollection, ...additionalFamilles);
      expect(comp.famillesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sousFamille: ISousFamille = { id: 456 };
      const sousFamille: ISousFamille = { id: 8130 };
      sousFamille.sousFamille = sousFamille;
      const famille: IFamille = { id: 91283 };
      sousFamille.famille = famille;

      activatedRoute.data = of({ sousFamille });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sousFamille));
      expect(comp.sousFamillesSharedCollection).toContain(sousFamille);
      expect(comp.famillesSharedCollection).toContain(famille);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousFamille>>();
      const sousFamille = { id: 123 };
      jest.spyOn(sousFamilleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousFamille });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousFamille }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sousFamilleService.update).toHaveBeenCalledWith(sousFamille);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousFamille>>();
      const sousFamille = new SousFamille();
      jest.spyOn(sousFamilleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousFamille });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousFamille }));
      saveSubject.complete();

      // THEN
      expect(sousFamilleService.create).toHaveBeenCalledWith(sousFamille);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousFamille>>();
      const sousFamille = { id: 123 };
      jest.spyOn(sousFamilleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousFamille });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sousFamilleService.update).toHaveBeenCalledWith(sousFamille);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSousFamilleById', () => {
      it('Should return tracked SousFamille primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousFamilleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFamilleById', () => {
      it('Should return tracked Famille primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFamilleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
