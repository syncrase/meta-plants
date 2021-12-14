jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SousEspeceService } from '../service/sous-espece.service';
import { ISousEspece, SousEspece } from '../sous-espece.model';
import { IEspece } from 'app/entities/classificationMS/espece/espece.model';
import { EspeceService } from 'app/entities/classificationMS/espece/service/espece.service';

import { SousEspeceUpdateComponent } from './sous-espece-update.component';

describe('SousEspece Management Update Component', () => {
  let comp: SousEspeceUpdateComponent;
  let fixture: ComponentFixture<SousEspeceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sousEspeceService: SousEspeceService;
  let especeService: EspeceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SousEspeceUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SousEspeceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SousEspeceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sousEspeceService = TestBed.inject(SousEspeceService);
    especeService = TestBed.inject(EspeceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SousEspece query and add missing value', () => {
      const sousEspece: ISousEspece = { id: 456 };
      const sousEspece: ISousEspece = { id: 31885 };
      sousEspece.sousEspece = sousEspece;

      const sousEspeceCollection: ISousEspece[] = [{ id: 99432 }];
      jest.spyOn(sousEspeceService, 'query').mockReturnValue(of(new HttpResponse({ body: sousEspeceCollection })));
      const additionalSousEspeces = [sousEspece];
      const expectedCollection: ISousEspece[] = [...additionalSousEspeces, ...sousEspeceCollection];
      jest.spyOn(sousEspeceService, 'addSousEspeceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousEspece });
      comp.ngOnInit();

      expect(sousEspeceService.query).toHaveBeenCalled();
      expect(sousEspeceService.addSousEspeceToCollectionIfMissing).toHaveBeenCalledWith(sousEspeceCollection, ...additionalSousEspeces);
      expect(comp.sousEspecesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Espece query and add missing value', () => {
      const sousEspece: ISousEspece = { id: 456 };
      const espece: IEspece = { id: 47976 };
      sousEspece.espece = espece;

      const especeCollection: IEspece[] = [{ id: 35715 }];
      jest.spyOn(especeService, 'query').mockReturnValue(of(new HttpResponse({ body: especeCollection })));
      const additionalEspeces = [espece];
      const expectedCollection: IEspece[] = [...additionalEspeces, ...especeCollection];
      jest.spyOn(especeService, 'addEspeceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousEspece });
      comp.ngOnInit();

      expect(especeService.query).toHaveBeenCalled();
      expect(especeService.addEspeceToCollectionIfMissing).toHaveBeenCalledWith(especeCollection, ...additionalEspeces);
      expect(comp.especesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sousEspece: ISousEspece = { id: 456 };
      const sousEspece: ISousEspece = { id: 33945 };
      sousEspece.sousEspece = sousEspece;
      const espece: IEspece = { id: 42721 };
      sousEspece.espece = espece;

      activatedRoute.data = of({ sousEspece });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sousEspece));
      expect(comp.sousEspecesSharedCollection).toContain(sousEspece);
      expect(comp.especesSharedCollection).toContain(espece);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousEspece>>();
      const sousEspece = { id: 123 };
      jest.spyOn(sousEspeceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousEspece });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousEspece }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sousEspeceService.update).toHaveBeenCalledWith(sousEspece);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousEspece>>();
      const sousEspece = new SousEspece();
      jest.spyOn(sousEspeceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousEspece });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousEspece }));
      saveSubject.complete();

      // THEN
      expect(sousEspeceService.create).toHaveBeenCalledWith(sousEspece);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousEspece>>();
      const sousEspece = { id: 123 };
      jest.spyOn(sousEspeceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousEspece });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sousEspeceService.update).toHaveBeenCalledWith(sousEspece);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSousEspeceById', () => {
      it('Should return tracked SousEspece primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousEspeceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackEspeceById', () => {
      it('Should return tracked Espece primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEspeceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
