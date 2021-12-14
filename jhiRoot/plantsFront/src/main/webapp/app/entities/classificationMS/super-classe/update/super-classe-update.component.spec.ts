jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SuperClasseService } from '../service/super-classe.service';
import { ISuperClasse, SuperClasse } from '../super-classe.model';
import { IMicroEmbranchement } from 'app/entities/classificationMS/micro-embranchement/micro-embranchement.model';
import { MicroEmbranchementService } from 'app/entities/classificationMS/micro-embranchement/service/micro-embranchement.service';

import { SuperClasseUpdateComponent } from './super-classe-update.component';

describe('SuperClasse Management Update Component', () => {
  let comp: SuperClasseUpdateComponent;
  let fixture: ComponentFixture<SuperClasseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let superClasseService: SuperClasseService;
  let microEmbranchementService: MicroEmbranchementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SuperClasseUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SuperClasseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SuperClasseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    superClasseService = TestBed.inject(SuperClasseService);
    microEmbranchementService = TestBed.inject(MicroEmbranchementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SuperClasse query and add missing value', () => {
      const superClasse: ISuperClasse = { id: 456 };
      const superClasse: ISuperClasse = { id: 2591 };
      superClasse.superClasse = superClasse;

      const superClasseCollection: ISuperClasse[] = [{ id: 28409 }];
      jest.spyOn(superClasseService, 'query').mockReturnValue(of(new HttpResponse({ body: superClasseCollection })));
      const additionalSuperClasses = [superClasse];
      const expectedCollection: ISuperClasse[] = [...additionalSuperClasses, ...superClasseCollection];
      jest.spyOn(superClasseService, 'addSuperClasseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ superClasse });
      comp.ngOnInit();

      expect(superClasseService.query).toHaveBeenCalled();
      expect(superClasseService.addSuperClasseToCollectionIfMissing).toHaveBeenCalledWith(superClasseCollection, ...additionalSuperClasses);
      expect(comp.superClassesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call MicroEmbranchement query and add missing value', () => {
      const superClasse: ISuperClasse = { id: 456 };
      const microEmbranchement: IMicroEmbranchement = { id: 77229 };
      superClasse.microEmbranchement = microEmbranchement;

      const microEmbranchementCollection: IMicroEmbranchement[] = [{ id: 1510 }];
      jest.spyOn(microEmbranchementService, 'query').mockReturnValue(of(new HttpResponse({ body: microEmbranchementCollection })));
      const additionalMicroEmbranchements = [microEmbranchement];
      const expectedCollection: IMicroEmbranchement[] = [...additionalMicroEmbranchements, ...microEmbranchementCollection];
      jest.spyOn(microEmbranchementService, 'addMicroEmbranchementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ superClasse });
      comp.ngOnInit();

      expect(microEmbranchementService.query).toHaveBeenCalled();
      expect(microEmbranchementService.addMicroEmbranchementToCollectionIfMissing).toHaveBeenCalledWith(
        microEmbranchementCollection,
        ...additionalMicroEmbranchements
      );
      expect(comp.microEmbranchementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const superClasse: ISuperClasse = { id: 456 };
      const superClasse: ISuperClasse = { id: 66796 };
      superClasse.superClasse = superClasse;
      const microEmbranchement: IMicroEmbranchement = { id: 48503 };
      superClasse.microEmbranchement = microEmbranchement;

      activatedRoute.data = of({ superClasse });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(superClasse));
      expect(comp.superClassesSharedCollection).toContain(superClasse);
      expect(comp.microEmbranchementsSharedCollection).toContain(microEmbranchement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SuperClasse>>();
      const superClasse = { id: 123 };
      jest.spyOn(superClasseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ superClasse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: superClasse }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(superClasseService.update).toHaveBeenCalledWith(superClasse);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SuperClasse>>();
      const superClasse = new SuperClasse();
      jest.spyOn(superClasseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ superClasse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: superClasse }));
      saveSubject.complete();

      // THEN
      expect(superClasseService.create).toHaveBeenCalledWith(superClasse);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SuperClasse>>();
      const superClasse = { id: 123 };
      jest.spyOn(superClasseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ superClasse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(superClasseService.update).toHaveBeenCalledWith(superClasse);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSuperClasseById', () => {
      it('Should return tracked SuperClasse primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSuperClasseById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackMicroEmbranchementById', () => {
      it('Should return tracked MicroEmbranchement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMicroEmbranchementById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
