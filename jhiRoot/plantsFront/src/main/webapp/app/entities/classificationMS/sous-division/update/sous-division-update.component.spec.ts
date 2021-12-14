jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SousDivisionService } from '../service/sous-division.service';
import { ISousDivision, SousDivision } from '../sous-division.model';
import { IDivision } from 'app/entities/classificationMS/division/division.model';
import { DivisionService } from 'app/entities/classificationMS/division/service/division.service';

import { SousDivisionUpdateComponent } from './sous-division-update.component';

describe('SousDivision Management Update Component', () => {
  let comp: SousDivisionUpdateComponent;
  let fixture: ComponentFixture<SousDivisionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sousDivisionService: SousDivisionService;
  let divisionService: DivisionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SousDivisionUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SousDivisionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SousDivisionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sousDivisionService = TestBed.inject(SousDivisionService);
    divisionService = TestBed.inject(DivisionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SousDivision query and add missing value', () => {
      const sousDivision: ISousDivision = { id: 456 };
      const sousDivision: ISousDivision = { id: 67043 };
      sousDivision.sousDivision = sousDivision;

      const sousDivisionCollection: ISousDivision[] = [{ id: 68544 }];
      jest.spyOn(sousDivisionService, 'query').mockReturnValue(of(new HttpResponse({ body: sousDivisionCollection })));
      const additionalSousDivisions = [sousDivision];
      const expectedCollection: ISousDivision[] = [...additionalSousDivisions, ...sousDivisionCollection];
      jest.spyOn(sousDivisionService, 'addSousDivisionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousDivision });
      comp.ngOnInit();

      expect(sousDivisionService.query).toHaveBeenCalled();
      expect(sousDivisionService.addSousDivisionToCollectionIfMissing).toHaveBeenCalledWith(
        sousDivisionCollection,
        ...additionalSousDivisions
      );
      expect(comp.sousDivisionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Division query and add missing value', () => {
      const sousDivision: ISousDivision = { id: 456 };
      const division: IDivision = { id: 58411 };
      sousDivision.division = division;

      const divisionCollection: IDivision[] = [{ id: 24471 }];
      jest.spyOn(divisionService, 'query').mockReturnValue(of(new HttpResponse({ body: divisionCollection })));
      const additionalDivisions = [division];
      const expectedCollection: IDivision[] = [...additionalDivisions, ...divisionCollection];
      jest.spyOn(divisionService, 'addDivisionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousDivision });
      comp.ngOnInit();

      expect(divisionService.query).toHaveBeenCalled();
      expect(divisionService.addDivisionToCollectionIfMissing).toHaveBeenCalledWith(divisionCollection, ...additionalDivisions);
      expect(comp.divisionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sousDivision: ISousDivision = { id: 456 };
      const sousDivision: ISousDivision = { id: 56443 };
      sousDivision.sousDivision = sousDivision;
      const division: IDivision = { id: 82507 };
      sousDivision.division = division;

      activatedRoute.data = of({ sousDivision });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sousDivision));
      expect(comp.sousDivisionsSharedCollection).toContain(sousDivision);
      expect(comp.divisionsSharedCollection).toContain(division);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousDivision>>();
      const sousDivision = { id: 123 };
      jest.spyOn(sousDivisionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousDivision });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousDivision }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sousDivisionService.update).toHaveBeenCalledWith(sousDivision);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousDivision>>();
      const sousDivision = new SousDivision();
      jest.spyOn(sousDivisionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousDivision });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousDivision }));
      saveSubject.complete();

      // THEN
      expect(sousDivisionService.create).toHaveBeenCalledWith(sousDivision);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousDivision>>();
      const sousDivision = { id: 123 };
      jest.spyOn(sousDivisionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousDivision });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sousDivisionService.update).toHaveBeenCalledWith(sousDivision);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSousDivisionById', () => {
      it('Should return tracked SousDivision primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousDivisionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDivisionById', () => {
      it('Should return tracked Division primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDivisionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
