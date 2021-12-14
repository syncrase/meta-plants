jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SuperFamilleService } from '../service/super-famille.service';
import { ISuperFamille, SuperFamille } from '../super-famille.model';
import { IMicroOrdre } from 'app/entities/classificationMS/micro-ordre/micro-ordre.model';
import { MicroOrdreService } from 'app/entities/classificationMS/micro-ordre/service/micro-ordre.service';

import { SuperFamilleUpdateComponent } from './super-famille-update.component';

describe('SuperFamille Management Update Component', () => {
  let comp: SuperFamilleUpdateComponent;
  let fixture: ComponentFixture<SuperFamilleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let superFamilleService: SuperFamilleService;
  let microOrdreService: MicroOrdreService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SuperFamilleUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SuperFamilleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SuperFamilleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    superFamilleService = TestBed.inject(SuperFamilleService);
    microOrdreService = TestBed.inject(MicroOrdreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SuperFamille query and add missing value', () => {
      const superFamille: ISuperFamille = { id: 456 };
      const superFamille: ISuperFamille = { id: 371 };
      superFamille.superFamille = superFamille;

      const superFamilleCollection: ISuperFamille[] = [{ id: 62092 }];
      jest.spyOn(superFamilleService, 'query').mockReturnValue(of(new HttpResponse({ body: superFamilleCollection })));
      const additionalSuperFamilles = [superFamille];
      const expectedCollection: ISuperFamille[] = [...additionalSuperFamilles, ...superFamilleCollection];
      jest.spyOn(superFamilleService, 'addSuperFamilleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ superFamille });
      comp.ngOnInit();

      expect(superFamilleService.query).toHaveBeenCalled();
      expect(superFamilleService.addSuperFamilleToCollectionIfMissing).toHaveBeenCalledWith(
        superFamilleCollection,
        ...additionalSuperFamilles
      );
      expect(comp.superFamillesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call MicroOrdre query and add missing value', () => {
      const superFamille: ISuperFamille = { id: 456 };
      const microOrdre: IMicroOrdre = { id: 594 };
      superFamille.microOrdre = microOrdre;

      const microOrdreCollection: IMicroOrdre[] = [{ id: 30892 }];
      jest.spyOn(microOrdreService, 'query').mockReturnValue(of(new HttpResponse({ body: microOrdreCollection })));
      const additionalMicroOrdres = [microOrdre];
      const expectedCollection: IMicroOrdre[] = [...additionalMicroOrdres, ...microOrdreCollection];
      jest.spyOn(microOrdreService, 'addMicroOrdreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ superFamille });
      comp.ngOnInit();

      expect(microOrdreService.query).toHaveBeenCalled();
      expect(microOrdreService.addMicroOrdreToCollectionIfMissing).toHaveBeenCalledWith(microOrdreCollection, ...additionalMicroOrdres);
      expect(comp.microOrdresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const superFamille: ISuperFamille = { id: 456 };
      const superFamille: ISuperFamille = { id: 84872 };
      superFamille.superFamille = superFamille;
      const microOrdre: IMicroOrdre = { id: 45868 };
      superFamille.microOrdre = microOrdre;

      activatedRoute.data = of({ superFamille });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(superFamille));
      expect(comp.superFamillesSharedCollection).toContain(superFamille);
      expect(comp.microOrdresSharedCollection).toContain(microOrdre);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SuperFamille>>();
      const superFamille = { id: 123 };
      jest.spyOn(superFamilleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ superFamille });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: superFamille }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(superFamilleService.update).toHaveBeenCalledWith(superFamille);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SuperFamille>>();
      const superFamille = new SuperFamille();
      jest.spyOn(superFamilleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ superFamille });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: superFamille }));
      saveSubject.complete();

      // THEN
      expect(superFamilleService.create).toHaveBeenCalledWith(superFamille);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SuperFamille>>();
      const superFamille = { id: 123 };
      jest.spyOn(superFamilleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ superFamille });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(superFamilleService.update).toHaveBeenCalledWith(superFamille);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSuperFamilleById', () => {
      it('Should return tracked SuperFamille primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSuperFamilleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackMicroOrdreById', () => {
      it('Should return tracked MicroOrdre primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMicroOrdreById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
