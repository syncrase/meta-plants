jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MicroEmbranchementService } from '../service/micro-embranchement.service';
import { IMicroEmbranchement, MicroEmbranchement } from '../micro-embranchement.model';
import { IInfraEmbranchement } from 'app/entities/classificationMS/infra-embranchement/infra-embranchement.model';
import { InfraEmbranchementService } from 'app/entities/classificationMS/infra-embranchement/service/infra-embranchement.service';

import { MicroEmbranchementUpdateComponent } from './micro-embranchement-update.component';

describe('MicroEmbranchement Management Update Component', () => {
  let comp: MicroEmbranchementUpdateComponent;
  let fixture: ComponentFixture<MicroEmbranchementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let microEmbranchementService: MicroEmbranchementService;
  let infraEmbranchementService: InfraEmbranchementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MicroEmbranchementUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(MicroEmbranchementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MicroEmbranchementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    microEmbranchementService = TestBed.inject(MicroEmbranchementService);
    infraEmbranchementService = TestBed.inject(InfraEmbranchementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MicroEmbranchement query and add missing value', () => {
      const microEmbranchement: IMicroEmbranchement = { id: 456 };
      const microEmbranchement: IMicroEmbranchement = { id: 67683 };
      microEmbranchement.microEmbranchement = microEmbranchement;

      const microEmbranchementCollection: IMicroEmbranchement[] = [{ id: 48043 }];
      jest.spyOn(microEmbranchementService, 'query').mockReturnValue(of(new HttpResponse({ body: microEmbranchementCollection })));
      const additionalMicroEmbranchements = [microEmbranchement];
      const expectedCollection: IMicroEmbranchement[] = [...additionalMicroEmbranchements, ...microEmbranchementCollection];
      jest.spyOn(microEmbranchementService, 'addMicroEmbranchementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ microEmbranchement });
      comp.ngOnInit();

      expect(microEmbranchementService.query).toHaveBeenCalled();
      expect(microEmbranchementService.addMicroEmbranchementToCollectionIfMissing).toHaveBeenCalledWith(
        microEmbranchementCollection,
        ...additionalMicroEmbranchements
      );
      expect(comp.microEmbranchementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call InfraEmbranchement query and add missing value', () => {
      const microEmbranchement: IMicroEmbranchement = { id: 456 };
      const infraEmbranchement: IInfraEmbranchement = { id: 26365 };
      microEmbranchement.infraEmbranchement = infraEmbranchement;

      const infraEmbranchementCollection: IInfraEmbranchement[] = [{ id: 21332 }];
      jest.spyOn(infraEmbranchementService, 'query').mockReturnValue(of(new HttpResponse({ body: infraEmbranchementCollection })));
      const additionalInfraEmbranchements = [infraEmbranchement];
      const expectedCollection: IInfraEmbranchement[] = [...additionalInfraEmbranchements, ...infraEmbranchementCollection];
      jest.spyOn(infraEmbranchementService, 'addInfraEmbranchementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ microEmbranchement });
      comp.ngOnInit();

      expect(infraEmbranchementService.query).toHaveBeenCalled();
      expect(infraEmbranchementService.addInfraEmbranchementToCollectionIfMissing).toHaveBeenCalledWith(
        infraEmbranchementCollection,
        ...additionalInfraEmbranchements
      );
      expect(comp.infraEmbranchementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const microEmbranchement: IMicroEmbranchement = { id: 456 };
      const microEmbranchement: IMicroEmbranchement = { id: 85767 };
      microEmbranchement.microEmbranchement = microEmbranchement;
      const infraEmbranchement: IInfraEmbranchement = { id: 60782 };
      microEmbranchement.infraEmbranchement = infraEmbranchement;

      activatedRoute.data = of({ microEmbranchement });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(microEmbranchement));
      expect(comp.microEmbranchementsSharedCollection).toContain(microEmbranchement);
      expect(comp.infraEmbranchementsSharedCollection).toContain(infraEmbranchement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MicroEmbranchement>>();
      const microEmbranchement = { id: 123 };
      jest.spyOn(microEmbranchementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ microEmbranchement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: microEmbranchement }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(microEmbranchementService.update).toHaveBeenCalledWith(microEmbranchement);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MicroEmbranchement>>();
      const microEmbranchement = new MicroEmbranchement();
      jest.spyOn(microEmbranchementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ microEmbranchement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: microEmbranchement }));
      saveSubject.complete();

      // THEN
      expect(microEmbranchementService.create).toHaveBeenCalledWith(microEmbranchement);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MicroEmbranchement>>();
      const microEmbranchement = { id: 123 };
      jest.spyOn(microEmbranchementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ microEmbranchement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(microEmbranchementService.update).toHaveBeenCalledWith(microEmbranchement);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMicroEmbranchementById', () => {
      it('Should return tracked MicroEmbranchement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMicroEmbranchementById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackInfraEmbranchementById', () => {
      it('Should return tracked InfraEmbranchement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInfraEmbranchementById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
