jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { InfraRegneService } from '../service/infra-regne.service';
import { IInfraRegne, InfraRegne } from '../infra-regne.model';
import { IRameau } from 'app/entities/classificationMS/rameau/rameau.model';
import { RameauService } from 'app/entities/classificationMS/rameau/service/rameau.service';

import { InfraRegneUpdateComponent } from './infra-regne-update.component';

describe('InfraRegne Management Update Component', () => {
  let comp: InfraRegneUpdateComponent;
  let fixture: ComponentFixture<InfraRegneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let infraRegneService: InfraRegneService;
  let rameauService: RameauService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [InfraRegneUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(InfraRegneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InfraRegneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    infraRegneService = TestBed.inject(InfraRegneService);
    rameauService = TestBed.inject(RameauService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InfraRegne query and add missing value', () => {
      const infraRegne: IInfraRegne = { id: 456 };
      const infraRegne: IInfraRegne = { id: 96221 };
      infraRegne.infraRegne = infraRegne;

      const infraRegneCollection: IInfraRegne[] = [{ id: 65823 }];
      jest.spyOn(infraRegneService, 'query').mockReturnValue(of(new HttpResponse({ body: infraRegneCollection })));
      const additionalInfraRegnes = [infraRegne];
      const expectedCollection: IInfraRegne[] = [...additionalInfraRegnes, ...infraRegneCollection];
      jest.spyOn(infraRegneService, 'addInfraRegneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ infraRegne });
      comp.ngOnInit();

      expect(infraRegneService.query).toHaveBeenCalled();
      expect(infraRegneService.addInfraRegneToCollectionIfMissing).toHaveBeenCalledWith(infraRegneCollection, ...additionalInfraRegnes);
      expect(comp.infraRegnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Rameau query and add missing value', () => {
      const infraRegne: IInfraRegne = { id: 456 };
      const rameau: IRameau = { id: 47281 };
      infraRegne.rameau = rameau;

      const rameauCollection: IRameau[] = [{ id: 60216 }];
      jest.spyOn(rameauService, 'query').mockReturnValue(of(new HttpResponse({ body: rameauCollection })));
      const additionalRameaus = [rameau];
      const expectedCollection: IRameau[] = [...additionalRameaus, ...rameauCollection];
      jest.spyOn(rameauService, 'addRameauToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ infraRegne });
      comp.ngOnInit();

      expect(rameauService.query).toHaveBeenCalled();
      expect(rameauService.addRameauToCollectionIfMissing).toHaveBeenCalledWith(rameauCollection, ...additionalRameaus);
      expect(comp.rameausSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const infraRegne: IInfraRegne = { id: 456 };
      const infraRegne: IInfraRegne = { id: 66222 };
      infraRegne.infraRegne = infraRegne;
      const rameau: IRameau = { id: 95350 };
      infraRegne.rameau = rameau;

      activatedRoute.data = of({ infraRegne });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(infraRegne));
      expect(comp.infraRegnesSharedCollection).toContain(infraRegne);
      expect(comp.rameausSharedCollection).toContain(rameau);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InfraRegne>>();
      const infraRegne = { id: 123 };
      jest.spyOn(infraRegneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infraRegne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: infraRegne }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(infraRegneService.update).toHaveBeenCalledWith(infraRegne);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InfraRegne>>();
      const infraRegne = new InfraRegne();
      jest.spyOn(infraRegneService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infraRegne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: infraRegne }));
      saveSubject.complete();

      // THEN
      expect(infraRegneService.create).toHaveBeenCalledWith(infraRegne);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<InfraRegne>>();
      const infraRegne = { id: 123 };
      jest.spyOn(infraRegneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infraRegne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(infraRegneService.update).toHaveBeenCalledWith(infraRegne);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackInfraRegneById', () => {
      it('Should return tracked InfraRegne primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInfraRegneById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackRameauById', () => {
      it('Should return tracked Rameau primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRameauById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
