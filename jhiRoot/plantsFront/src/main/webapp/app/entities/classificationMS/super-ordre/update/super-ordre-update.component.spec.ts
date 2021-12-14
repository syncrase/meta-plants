jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SuperOrdreService } from '../service/super-ordre.service';
import { ISuperOrdre, SuperOrdre } from '../super-ordre.model';
import { IInfraClasse } from 'app/entities/classificationMS/infra-classe/infra-classe.model';
import { InfraClasseService } from 'app/entities/classificationMS/infra-classe/service/infra-classe.service';

import { SuperOrdreUpdateComponent } from './super-ordre-update.component';

describe('SuperOrdre Management Update Component', () => {
  let comp: SuperOrdreUpdateComponent;
  let fixture: ComponentFixture<SuperOrdreUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let superOrdreService: SuperOrdreService;
  let infraClasseService: InfraClasseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SuperOrdreUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SuperOrdreUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SuperOrdreUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    superOrdreService = TestBed.inject(SuperOrdreService);
    infraClasseService = TestBed.inject(InfraClasseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SuperOrdre query and add missing value', () => {
      const superOrdre: ISuperOrdre = { id: 456 };
      const superOrdre: ISuperOrdre = { id: 48673 };
      superOrdre.superOrdre = superOrdre;

      const superOrdreCollection: ISuperOrdre[] = [{ id: 30574 }];
      jest.spyOn(superOrdreService, 'query').mockReturnValue(of(new HttpResponse({ body: superOrdreCollection })));
      const additionalSuperOrdres = [superOrdre];
      const expectedCollection: ISuperOrdre[] = [...additionalSuperOrdres, ...superOrdreCollection];
      jest.spyOn(superOrdreService, 'addSuperOrdreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ superOrdre });
      comp.ngOnInit();

      expect(superOrdreService.query).toHaveBeenCalled();
      expect(superOrdreService.addSuperOrdreToCollectionIfMissing).toHaveBeenCalledWith(superOrdreCollection, ...additionalSuperOrdres);
      expect(comp.superOrdresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call InfraClasse query and add missing value', () => {
      const superOrdre: ISuperOrdre = { id: 456 };
      const infraClasse: IInfraClasse = { id: 63793 };
      superOrdre.infraClasse = infraClasse;

      const infraClasseCollection: IInfraClasse[] = [{ id: 72455 }];
      jest.spyOn(infraClasseService, 'query').mockReturnValue(of(new HttpResponse({ body: infraClasseCollection })));
      const additionalInfraClasses = [infraClasse];
      const expectedCollection: IInfraClasse[] = [...additionalInfraClasses, ...infraClasseCollection];
      jest.spyOn(infraClasseService, 'addInfraClasseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ superOrdre });
      comp.ngOnInit();

      expect(infraClasseService.query).toHaveBeenCalled();
      expect(infraClasseService.addInfraClasseToCollectionIfMissing).toHaveBeenCalledWith(infraClasseCollection, ...additionalInfraClasses);
      expect(comp.infraClassesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const superOrdre: ISuperOrdre = { id: 456 };
      const superOrdre: ISuperOrdre = { id: 68921 };
      superOrdre.superOrdre = superOrdre;
      const infraClasse: IInfraClasse = { id: 23284 };
      superOrdre.infraClasse = infraClasse;

      activatedRoute.data = of({ superOrdre });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(superOrdre));
      expect(comp.superOrdresSharedCollection).toContain(superOrdre);
      expect(comp.infraClassesSharedCollection).toContain(infraClasse);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SuperOrdre>>();
      const superOrdre = { id: 123 };
      jest.spyOn(superOrdreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ superOrdre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: superOrdre }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(superOrdreService.update).toHaveBeenCalledWith(superOrdre);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SuperOrdre>>();
      const superOrdre = new SuperOrdre();
      jest.spyOn(superOrdreService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ superOrdre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: superOrdre }));
      saveSubject.complete();

      // THEN
      expect(superOrdreService.create).toHaveBeenCalledWith(superOrdre);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SuperOrdre>>();
      const superOrdre = { id: 123 };
      jest.spyOn(superOrdreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ superOrdre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(superOrdreService.update).toHaveBeenCalledWith(superOrdre);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSuperOrdreById', () => {
      it('Should return tracked SuperOrdre primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSuperOrdreById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackInfraClasseById', () => {
      it('Should return tracked InfraClasse primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInfraClasseById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
