jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InfraClasseService } from '../service/infra-classe.service';
import { IInfraClasse, InfraClasse } from '../infra-classe.model';
import { ISousClasse } from 'app/entities/classificationMS/sous-classe/sous-classe.model';
import { SousClasseService } from 'app/entities/classificationMS/sous-classe/service/sous-classe.service';

import { InfraClasseUpdateComponent } from './infra-classe-update.component';

describe('InfraClasse Management Update Component', () => {
  let comp: InfraClasseUpdateComponent;
  let fixture: ComponentFixture<InfraClasseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let infraClasseService: InfraClasseService;
  let sousClasseService: SousClasseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [InfraClasseUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(InfraClasseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InfraClasseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    infraClasseService = TestBed.inject(InfraClasseService);
    sousClasseService = TestBed.inject(SousClasseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InfraClasse query and add missing value', () => {
      const infraClasse: IInfraClasse = { id: 456 };
      const infraClasse: IInfraClasse = { id: 19897 };
      infraClasse.infraClasse = infraClasse;

      const infraClasseCollection: IInfraClasse[] = [{ id: 21731 }];
      jest.spyOn(infraClasseService, 'query').mockReturnValue(of(new HttpResponse({ body: infraClasseCollection })));
      const additionalInfraClasses = [infraClasse];
      const expectedCollection: IInfraClasse[] = [...additionalInfraClasses, ...infraClasseCollection];
      jest.spyOn(infraClasseService, 'addInfraClasseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ infraClasse });
      comp.ngOnInit();

      expect(infraClasseService.query).toHaveBeenCalled();
      expect(infraClasseService.addInfraClasseToCollectionIfMissing).toHaveBeenCalledWith(infraClasseCollection, ...additionalInfraClasses);
      expect(comp.infraClassesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SousClasse query and add missing value', () => {
      const infraClasse: IInfraClasse = { id: 456 };
      const sousClasse: ISousClasse = { id: 4103 };
      infraClasse.sousClasse = sousClasse;

      const sousClasseCollection: ISousClasse[] = [{ id: 44837 }];
      jest.spyOn(sousClasseService, 'query').mockReturnValue(of(new HttpResponse({ body: sousClasseCollection })));
      const additionalSousClasses = [sousClasse];
      const expectedCollection: ISousClasse[] = [...additionalSousClasses, ...sousClasseCollection];
      jest.spyOn(sousClasseService, 'addSousClasseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ infraClasse });
      comp.ngOnInit();

      expect(sousClasseService.query).toHaveBeenCalled();
      expect(sousClasseService.addSousClasseToCollectionIfMissing).toHaveBeenCalledWith(sousClasseCollection, ...additionalSousClasses);
      expect(comp.sousClassesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const infraClasse: IInfraClasse = { id: 456 };
      const infraClasse: IInfraClasse = { id: 54077 };
      infraClasse.infraClasse = infraClasse;
      const sousClasse: ISousClasse = { id: 71055 };
      infraClasse.sousClasse = sousClasse;

      activatedRoute.data = of({ infraClasse });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(infraClasse));
      expect(comp.infraClassesSharedCollection).toContain(infraClasse);
      expect(comp.sousClassesSharedCollection).toContain(sousClasse);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InfraClasse>>();
      const infraClasse = { id: 123 };
      jest.spyOn(infraClasseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infraClasse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: infraClasse }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(infraClasseService.update).toHaveBeenCalledWith(infraClasse);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InfraClasse>>();
      const infraClasse = new InfraClasse();
      jest.spyOn(infraClasseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infraClasse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: infraClasse }));
      saveSubject.complete();

      // THEN
      expect(infraClasseService.create).toHaveBeenCalledWith(infraClasse);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InfraClasse>>();
      const infraClasse = { id: 123 };
      jest.spyOn(infraClasseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infraClasse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(infraClasseService.update).toHaveBeenCalledWith(infraClasse);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackInfraClasseById', () => {
      it('Should return tracked InfraClasse primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInfraClasseById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSousClasseById', () => {
      it('Should return tracked SousClasse primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousClasseById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
