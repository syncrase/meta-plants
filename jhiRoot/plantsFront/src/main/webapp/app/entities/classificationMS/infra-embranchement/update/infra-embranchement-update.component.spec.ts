jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InfraEmbranchementService } from '../service/infra-embranchement.service';
import { IInfraEmbranchement, InfraEmbranchement } from '../infra-embranchement.model';
import { ISousDivision } from 'app/entities/classificationMS/sous-division/sous-division.model';
import { SousDivisionService } from 'app/entities/classificationMS/sous-division/service/sous-division.service';

import { InfraEmbranchementUpdateComponent } from './infra-embranchement-update.component';

describe('InfraEmbranchement Management Update Component', () => {
  let comp: InfraEmbranchementUpdateComponent;
  let fixture: ComponentFixture<InfraEmbranchementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let infraEmbranchementService: InfraEmbranchementService;
  let sousDivisionService: SousDivisionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [InfraEmbranchementUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(InfraEmbranchementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InfraEmbranchementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    infraEmbranchementService = TestBed.inject(InfraEmbranchementService);
    sousDivisionService = TestBed.inject(SousDivisionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InfraEmbranchement query and add missing value', () => {
      const infraEmbranchement: IInfraEmbranchement = { id: 456 };
      const infraEmbranchement: IInfraEmbranchement = { id: 32915 };
      infraEmbranchement.infraEmbranchement = infraEmbranchement;

      const infraEmbranchementCollection: IInfraEmbranchement[] = [{ id: 61083 }];
      jest.spyOn(infraEmbranchementService, 'query').mockReturnValue(of(new HttpResponse({ body: infraEmbranchementCollection })));
      const additionalInfraEmbranchements = [infraEmbranchement];
      const expectedCollection: IInfraEmbranchement[] = [...additionalInfraEmbranchements, ...infraEmbranchementCollection];
      jest.spyOn(infraEmbranchementService, 'addInfraEmbranchementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ infraEmbranchement });
      comp.ngOnInit();

      expect(infraEmbranchementService.query).toHaveBeenCalled();
      expect(infraEmbranchementService.addInfraEmbranchementToCollectionIfMissing).toHaveBeenCalledWith(
        infraEmbranchementCollection,
        ...additionalInfraEmbranchements
      );
      expect(comp.infraEmbranchementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SousDivision query and add missing value', () => {
      const infraEmbranchement: IInfraEmbranchement = { id: 456 };
      const sousDivision: ISousDivision = { id: 54578 };
      infraEmbranchement.sousDivision = sousDivision;

      const sousDivisionCollection: ISousDivision[] = [{ id: 6071 }];
      jest.spyOn(sousDivisionService, 'query').mockReturnValue(of(new HttpResponse({ body: sousDivisionCollection })));
      const additionalSousDivisions = [sousDivision];
      const expectedCollection: ISousDivision[] = [...additionalSousDivisions, ...sousDivisionCollection];
      jest.spyOn(sousDivisionService, 'addSousDivisionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ infraEmbranchement });
      comp.ngOnInit();

      expect(sousDivisionService.query).toHaveBeenCalled();
      expect(sousDivisionService.addSousDivisionToCollectionIfMissing).toHaveBeenCalledWith(
        sousDivisionCollection,
        ...additionalSousDivisions
      );
      expect(comp.sousDivisionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const infraEmbranchement: IInfraEmbranchement = { id: 456 };
      const infraEmbranchement: IInfraEmbranchement = { id: 298 };
      infraEmbranchement.infraEmbranchement = infraEmbranchement;
      const sousDivision: ISousDivision = { id: 93268 };
      infraEmbranchement.sousDivision = sousDivision;

      activatedRoute.data = of({ infraEmbranchement });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(infraEmbranchement));
      expect(comp.infraEmbranchementsSharedCollection).toContain(infraEmbranchement);
      expect(comp.sousDivisionsSharedCollection).toContain(sousDivision);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InfraEmbranchement>>();
      const infraEmbranchement = { id: 123 };
      jest.spyOn(infraEmbranchementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infraEmbranchement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: infraEmbranchement }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(infraEmbranchementService.update).toHaveBeenCalledWith(infraEmbranchement);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InfraEmbranchement>>();
      const infraEmbranchement = new InfraEmbranchement();
      jest.spyOn(infraEmbranchementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infraEmbranchement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: infraEmbranchement }));
      saveSubject.complete();

      // THEN
      expect(infraEmbranchementService.create).toHaveBeenCalledWith(infraEmbranchement);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InfraEmbranchement>>();
      const infraEmbranchement = { id: 123 };
      jest.spyOn(infraEmbranchementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infraEmbranchement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(infraEmbranchementService.update).toHaveBeenCalledWith(infraEmbranchement);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackInfraEmbranchementById', () => {
      it('Should return tracked InfraEmbranchement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInfraEmbranchementById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSousDivisionById', () => {
      it('Should return tracked SousDivision primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousDivisionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
