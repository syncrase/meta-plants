jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MicroOrdreService } from '../service/micro-ordre.service';
import { IMicroOrdre, MicroOrdre } from '../micro-ordre.model';
import { IInfraOrdre } from 'app/entities/classificationMS/infra-ordre/infra-ordre.model';
import { InfraOrdreService } from 'app/entities/classificationMS/infra-ordre/service/infra-ordre.service';

import { MicroOrdreUpdateComponent } from './micro-ordre-update.component';

describe('MicroOrdre Management Update Component', () => {
  let comp: MicroOrdreUpdateComponent;
  let fixture: ComponentFixture<MicroOrdreUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let microOrdreService: MicroOrdreService;
  let infraOrdreService: InfraOrdreService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [MicroOrdreUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(MicroOrdreUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MicroOrdreUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    microOrdreService = TestBed.inject(MicroOrdreService);
    infraOrdreService = TestBed.inject(InfraOrdreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MicroOrdre query and add missing value', () => {
      const microOrdre: IMicroOrdre = { id: 456 };
      const microOrdre: IMicroOrdre = { id: 73520 };
      microOrdre.microOrdre = microOrdre;

      const microOrdreCollection: IMicroOrdre[] = [{ id: 90651 }];
      jest.spyOn(microOrdreService, 'query').mockReturnValue(of(new HttpResponse({ body: microOrdreCollection })));
      const additionalMicroOrdres = [microOrdre];
      const expectedCollection: IMicroOrdre[] = [...additionalMicroOrdres, ...microOrdreCollection];
      jest.spyOn(microOrdreService, 'addMicroOrdreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ microOrdre });
      comp.ngOnInit();

      expect(microOrdreService.query).toHaveBeenCalled();
      expect(microOrdreService.addMicroOrdreToCollectionIfMissing).toHaveBeenCalledWith(microOrdreCollection, ...additionalMicroOrdres);
      expect(comp.microOrdresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call InfraOrdre query and add missing value', () => {
      const microOrdre: IMicroOrdre = { id: 456 };
      const infraOrdre: IInfraOrdre = { id: 46469 };
      microOrdre.infraOrdre = infraOrdre;

      const infraOrdreCollection: IInfraOrdre[] = [{ id: 78587 }];
      jest.spyOn(infraOrdreService, 'query').mockReturnValue(of(new HttpResponse({ body: infraOrdreCollection })));
      const additionalInfraOrdres = [infraOrdre];
      const expectedCollection: IInfraOrdre[] = [...additionalInfraOrdres, ...infraOrdreCollection];
      jest.spyOn(infraOrdreService, 'addInfraOrdreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ microOrdre });
      comp.ngOnInit();

      expect(infraOrdreService.query).toHaveBeenCalled();
      expect(infraOrdreService.addInfraOrdreToCollectionIfMissing).toHaveBeenCalledWith(infraOrdreCollection, ...additionalInfraOrdres);
      expect(comp.infraOrdresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const microOrdre: IMicroOrdre = { id: 456 };
      const microOrdre: IMicroOrdre = { id: 75736 };
      microOrdre.microOrdre = microOrdre;
      const infraOrdre: IInfraOrdre = { id: 35015 };
      microOrdre.infraOrdre = infraOrdre;

      activatedRoute.data = of({ microOrdre });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(microOrdre));
      expect(comp.microOrdresSharedCollection).toContain(microOrdre);
      expect(comp.infraOrdresSharedCollection).toContain(infraOrdre);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MicroOrdre>>();
      const microOrdre = { id: 123 };
      jest.spyOn(microOrdreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ microOrdre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: microOrdre }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(microOrdreService.update).toHaveBeenCalledWith(microOrdre);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MicroOrdre>>();
      const microOrdre = new MicroOrdre();
      jest.spyOn(microOrdreService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ microOrdre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: microOrdre }));
      saveSubject.complete();

      // THEN
      expect(microOrdreService.create).toHaveBeenCalledWith(microOrdre);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MicroOrdre>>();
      const microOrdre = { id: 123 };
      jest.spyOn(microOrdreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ microOrdre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(microOrdreService.update).toHaveBeenCalledWith(microOrdre);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackMicroOrdreById', () => {
      it('Should return tracked MicroOrdre primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMicroOrdreById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackInfraOrdreById', () => {
      it('Should return tracked InfraOrdre primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInfraOrdreById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
