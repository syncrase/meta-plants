jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SuperDivisionService } from '../service/super-division.service';
import { ISuperDivision, SuperDivision } from '../super-division.model';
import { IInfraRegne } from 'app/entities/classificationMS/infra-regne/infra-regne.model';
import { InfraRegneService } from 'app/entities/classificationMS/infra-regne/service/infra-regne.service';

import { SuperDivisionUpdateComponent } from './super-division-update.component';

describe('SuperDivision Management Update Component', () => {
  let comp: SuperDivisionUpdateComponent;
  let fixture: ComponentFixture<SuperDivisionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let superDivisionService: SuperDivisionService;
  let infraRegneService: InfraRegneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SuperDivisionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SuperDivisionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SuperDivisionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    superDivisionService = TestBed.inject(SuperDivisionService);
    infraRegneService = TestBed.inject(InfraRegneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SuperDivision query and add missing value', () => {
      const superDivision: ISuperDivision = { id: 456 };
      const superDivision: ISuperDivision = { id: 30640 };
      superDivision.superDivision = superDivision;

      const superDivisionCollection: ISuperDivision[] = [{ id: 9858 }];
      jest.spyOn(superDivisionService, 'query').mockReturnValue(of(new HttpResponse({ body: superDivisionCollection })));
      const additionalSuperDivisions = [superDivision];
      const expectedCollection: ISuperDivision[] = [...additionalSuperDivisions, ...superDivisionCollection];
      jest.spyOn(superDivisionService, 'addSuperDivisionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ superDivision });
      comp.ngOnInit();

      expect(superDivisionService.query).toHaveBeenCalled();
      expect(superDivisionService.addSuperDivisionToCollectionIfMissing).toHaveBeenCalledWith(
        superDivisionCollection,
        ...additionalSuperDivisions
      );
      expect(comp.superDivisionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call InfraRegne query and add missing value', () => {
      const superDivision: ISuperDivision = { id: 456 };
      const infraRegne: IInfraRegne = { id: 97523 };
      superDivision.infraRegne = infraRegne;

      const infraRegneCollection: IInfraRegne[] = [{ id: 59406 }];
      jest.spyOn(infraRegneService, 'query').mockReturnValue(of(new HttpResponse({ body: infraRegneCollection })));
      const additionalInfraRegnes = [infraRegne];
      const expectedCollection: IInfraRegne[] = [...additionalInfraRegnes, ...infraRegneCollection];
      jest.spyOn(infraRegneService, 'addInfraRegneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ superDivision });
      comp.ngOnInit();

      expect(infraRegneService.query).toHaveBeenCalled();
      expect(infraRegneService.addInfraRegneToCollectionIfMissing).toHaveBeenCalledWith(infraRegneCollection, ...additionalInfraRegnes);
      expect(comp.infraRegnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const superDivision: ISuperDivision = { id: 456 };
      const superDivision: ISuperDivision = { id: 96239 };
      superDivision.superDivision = superDivision;
      const infraRegne: IInfraRegne = { id: 50265 };
      superDivision.infraRegne = infraRegne;

      activatedRoute.data = of({ superDivision });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(superDivision));
      expect(comp.superDivisionsSharedCollection).toContain(superDivision);
      expect(comp.infraRegnesSharedCollection).toContain(infraRegne);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SuperDivision>>();
      const superDivision = { id: 123 };
      jest.spyOn(superDivisionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ superDivision });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: superDivision }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(superDivisionService.update).toHaveBeenCalledWith(superDivision);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SuperDivision>>();
      const superDivision = new SuperDivision();
      jest.spyOn(superDivisionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ superDivision });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: superDivision }));
      saveSubject.complete();

      // THEN
      expect(superDivisionService.create).toHaveBeenCalledWith(superDivision);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SuperDivision>>();
      const superDivision = { id: 123 };
      jest.spyOn(superDivisionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ superDivision });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(superDivisionService.update).toHaveBeenCalledWith(superDivision);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSuperDivisionById', () => {
      it('Should return tracked SuperDivision primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSuperDivisionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackInfraRegneById', () => {
      it('Should return tracked InfraRegne primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInfraRegneById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
