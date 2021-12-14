jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SousTribuService } from '../service/sous-tribu.service';
import { ISousTribu, SousTribu } from '../sous-tribu.model';
import { ITribu } from 'app/entities/classificationMS/tribu/tribu.model';
import { TribuService } from 'app/entities/classificationMS/tribu/service/tribu.service';

import { SousTribuUpdateComponent } from './sous-tribu-update.component';

describe('SousTribu Management Update Component', () => {
  let comp: SousTribuUpdateComponent;
  let fixture: ComponentFixture<SousTribuUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sousTribuService: SousTribuService;
  let tribuService: TribuService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SousTribuUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SousTribuUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SousTribuUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sousTribuService = TestBed.inject(SousTribuService);
    tribuService = TestBed.inject(TribuService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SousTribu query and add missing value', () => {
      const sousTribu: ISousTribu = { id: 456 };
      const sousTribu: ISousTribu = { id: 29240 };
      sousTribu.sousTribu = sousTribu;

      const sousTribuCollection: ISousTribu[] = [{ id: 57575 }];
      jest.spyOn(sousTribuService, 'query').mockReturnValue(of(new HttpResponse({ body: sousTribuCollection })));
      const additionalSousTribus = [sousTribu];
      const expectedCollection: ISousTribu[] = [...additionalSousTribus, ...sousTribuCollection];
      jest.spyOn(sousTribuService, 'addSousTribuToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousTribu });
      comp.ngOnInit();

      expect(sousTribuService.query).toHaveBeenCalled();
      expect(sousTribuService.addSousTribuToCollectionIfMissing).toHaveBeenCalledWith(sousTribuCollection, ...additionalSousTribus);
      expect(comp.sousTribusSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Tribu query and add missing value', () => {
      const sousTribu: ISousTribu = { id: 456 };
      const tribu: ITribu = { id: 6333 };
      sousTribu.tribu = tribu;

      const tribuCollection: ITribu[] = [{ id: 82931 }];
      jest.spyOn(tribuService, 'query').mockReturnValue(of(new HttpResponse({ body: tribuCollection })));
      const additionalTribus = [tribu];
      const expectedCollection: ITribu[] = [...additionalTribus, ...tribuCollection];
      jest.spyOn(tribuService, 'addTribuToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousTribu });
      comp.ngOnInit();

      expect(tribuService.query).toHaveBeenCalled();
      expect(tribuService.addTribuToCollectionIfMissing).toHaveBeenCalledWith(tribuCollection, ...additionalTribus);
      expect(comp.tribusSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sousTribu: ISousTribu = { id: 456 };
      const sousTribu: ISousTribu = { id: 84886 };
      sousTribu.sousTribu = sousTribu;
      const tribu: ITribu = { id: 41679 };
      sousTribu.tribu = tribu;

      activatedRoute.data = of({ sousTribu });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sousTribu));
      expect(comp.sousTribusSharedCollection).toContain(sousTribu);
      expect(comp.tribusSharedCollection).toContain(tribu);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousTribu>>();
      const sousTribu = { id: 123 };
      jest.spyOn(sousTribuService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousTribu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousTribu }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sousTribuService.update).toHaveBeenCalledWith(sousTribu);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousTribu>>();
      const sousTribu = new SousTribu();
      jest.spyOn(sousTribuService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousTribu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousTribu }));
      saveSubject.complete();

      // THEN
      expect(sousTribuService.create).toHaveBeenCalledWith(sousTribu);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousTribu>>();
      const sousTribu = { id: 123 };
      jest.spyOn(sousTribuService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousTribu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sousTribuService.update).toHaveBeenCalledWith(sousTribu);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSousTribuById', () => {
      it('Should return tracked SousTribu primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousTribuById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTribuById', () => {
      it('Should return tracked Tribu primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTribuById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
