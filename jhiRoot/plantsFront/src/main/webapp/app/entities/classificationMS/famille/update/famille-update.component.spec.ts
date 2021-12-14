jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FamilleService } from '../service/famille.service';
import { IFamille, Famille } from '../famille.model';
import { ISuperFamille } from 'app/entities/classificationMS/super-famille/super-famille.model';
import { SuperFamilleService } from 'app/entities/classificationMS/super-famille/service/super-famille.service';

import { FamilleUpdateComponent } from './famille-update.component';

describe('Famille Management Update Component', () => {
  let comp: FamilleUpdateComponent;
  let fixture: ComponentFixture<FamilleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let familleService: FamilleService;
  let superFamilleService: SuperFamilleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FamilleUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FamilleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FamilleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    familleService = TestBed.inject(FamilleService);
    superFamilleService = TestBed.inject(SuperFamilleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Famille query and add missing value', () => {
      const famille: IFamille = { id: 456 };
      const famille: IFamille = { id: 34804 };
      famille.famille = famille;

      const familleCollection: IFamille[] = [{ id: 24972 }];
      jest.spyOn(familleService, 'query').mockReturnValue(of(new HttpResponse({ body: familleCollection })));
      const additionalFamilles = [famille];
      const expectedCollection: IFamille[] = [...additionalFamilles, ...familleCollection];
      jest.spyOn(familleService, 'addFamilleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      expect(familleService.query).toHaveBeenCalled();
      expect(familleService.addFamilleToCollectionIfMissing).toHaveBeenCalledWith(familleCollection, ...additionalFamilles);
      expect(comp.famillesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SuperFamille query and add missing value', () => {
      const famille: IFamille = { id: 456 };
      const superFamille: ISuperFamille = { id: 17118 };
      famille.superFamille = superFamille;

      const superFamilleCollection: ISuperFamille[] = [{ id: 48499 }];
      jest.spyOn(superFamilleService, 'query').mockReturnValue(of(new HttpResponse({ body: superFamilleCollection })));
      const additionalSuperFamilles = [superFamille];
      const expectedCollection: ISuperFamille[] = [...additionalSuperFamilles, ...superFamilleCollection];
      jest.spyOn(superFamilleService, 'addSuperFamilleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      expect(superFamilleService.query).toHaveBeenCalled();
      expect(superFamilleService.addSuperFamilleToCollectionIfMissing).toHaveBeenCalledWith(
        superFamilleCollection,
        ...additionalSuperFamilles
      );
      expect(comp.superFamillesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const famille: IFamille = { id: 456 };
      const famille: IFamille = { id: 15168 };
      famille.famille = famille;
      const superFamille: ISuperFamille = { id: 54301 };
      famille.superFamille = superFamille;

      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(famille));
      expect(comp.famillesSharedCollection).toContain(famille);
      expect(comp.superFamillesSharedCollection).toContain(superFamille);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Famille>>();
      const famille = { id: 123 };
      jest.spyOn(familleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: famille }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(familleService.update).toHaveBeenCalledWith(famille);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Famille>>();
      const famille = new Famille();
      jest.spyOn(familleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: famille }));
      saveSubject.complete();

      // THEN
      expect(familleService.create).toHaveBeenCalledWith(famille);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Famille>>();
      const famille = { id: 123 };
      jest.spyOn(familleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ famille });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(familleService.update).toHaveBeenCalledWith(famille);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFamilleById', () => {
      it('Should return tracked Famille primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFamilleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSuperFamilleById', () => {
      it('Should return tracked SuperFamille primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSuperFamilleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
