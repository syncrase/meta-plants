jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DivisionService } from '../service/division.service';
import { IDivision, Division } from '../division.model';
import { ISuperDivision } from 'app/entities/classificationMS/super-division/super-division.model';
import { SuperDivisionService } from 'app/entities/classificationMS/super-division/service/super-division.service';

import { DivisionUpdateComponent } from './division-update.component';

describe('Division Management Update Component', () => {
  let comp: DivisionUpdateComponent;
  let fixture: ComponentFixture<DivisionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let divisionService: DivisionService;
  let superDivisionService: SuperDivisionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DivisionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DivisionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DivisionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    divisionService = TestBed.inject(DivisionService);
    superDivisionService = TestBed.inject(SuperDivisionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Division query and add missing value', () => {
      const division: IDivision = { id: 456 };
      const division: IDivision = { id: 48024 };
      division.division = division;

      const divisionCollection: IDivision[] = [{ id: 4888 }];
      jest.spyOn(divisionService, 'query').mockReturnValue(of(new HttpResponse({ body: divisionCollection })));
      const additionalDivisions = [division];
      const expectedCollection: IDivision[] = [...additionalDivisions, ...divisionCollection];
      jest.spyOn(divisionService, 'addDivisionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ division });
      comp.ngOnInit();

      expect(divisionService.query).toHaveBeenCalled();
      expect(divisionService.addDivisionToCollectionIfMissing).toHaveBeenCalledWith(divisionCollection, ...additionalDivisions);
      expect(comp.divisionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SuperDivision query and add missing value', () => {
      const division: IDivision = { id: 456 };
      const superDivision: ISuperDivision = { id: 63560 };
      division.superDivision = superDivision;

      const superDivisionCollection: ISuperDivision[] = [{ id: 64309 }];
      jest.spyOn(superDivisionService, 'query').mockReturnValue(of(new HttpResponse({ body: superDivisionCollection })));
      const additionalSuperDivisions = [superDivision];
      const expectedCollection: ISuperDivision[] = [...additionalSuperDivisions, ...superDivisionCollection];
      jest.spyOn(superDivisionService, 'addSuperDivisionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ division });
      comp.ngOnInit();

      expect(superDivisionService.query).toHaveBeenCalled();
      expect(superDivisionService.addSuperDivisionToCollectionIfMissing).toHaveBeenCalledWith(
        superDivisionCollection,
        ...additionalSuperDivisions
      );
      expect(comp.superDivisionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const division: IDivision = { id: 456 };
      const division: IDivision = { id: 10939 };
      division.division = division;
      const superDivision: ISuperDivision = { id: 13176 };
      division.superDivision = superDivision;

      activatedRoute.data = of({ division });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(division));
      expect(comp.divisionsSharedCollection).toContain(division);
      expect(comp.superDivisionsSharedCollection).toContain(superDivision);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Division>>();
      const division = { id: 123 };
      jest.spyOn(divisionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ division });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: division }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(divisionService.update).toHaveBeenCalledWith(division);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Division>>();
      const division = new Division();
      jest.spyOn(divisionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ division });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: division }));
      saveSubject.complete();

      // THEN
      expect(divisionService.create).toHaveBeenCalledWith(division);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Division>>();
      const division = { id: 123 };
      jest.spyOn(divisionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ division });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(divisionService.update).toHaveBeenCalledWith(division);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDivisionById', () => {
      it('Should return tracked Division primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDivisionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSuperDivisionById', () => {
      it('Should return tracked SuperDivision primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSuperDivisionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
