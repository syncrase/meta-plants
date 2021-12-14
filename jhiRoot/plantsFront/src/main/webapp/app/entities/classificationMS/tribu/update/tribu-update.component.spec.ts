jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TribuService } from '../service/tribu.service';
import { ITribu, Tribu } from '../tribu.model';
import { ISousFamille } from 'app/entities/classificationMS/sous-famille/sous-famille.model';
import { SousFamilleService } from 'app/entities/classificationMS/sous-famille/service/sous-famille.service';

import { TribuUpdateComponent } from './tribu-update.component';

describe('Tribu Management Update Component', () => {
  let comp: TribuUpdateComponent;
  let fixture: ComponentFixture<TribuUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tribuService: TribuService;
  let sousFamilleService: SousFamilleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [TribuUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(TribuUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TribuUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tribuService = TestBed.inject(TribuService);
    sousFamilleService = TestBed.inject(SousFamilleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Tribu query and add missing value', () => {
      const tribu: ITribu = { id: 456 };
      const tribu: ITribu = { id: 89481 };
      tribu.tribu = tribu;

      const tribuCollection: ITribu[] = [{ id: 25768 }];
      jest.spyOn(tribuService, 'query').mockReturnValue(of(new HttpResponse({ body: tribuCollection })));
      const additionalTribus = [tribu];
      const expectedCollection: ITribu[] = [...additionalTribus, ...tribuCollection];
      jest.spyOn(tribuService, 'addTribuToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tribu });
      comp.ngOnInit();

      expect(tribuService.query).toHaveBeenCalled();
      expect(tribuService.addTribuToCollectionIfMissing).toHaveBeenCalledWith(tribuCollection, ...additionalTribus);
      expect(comp.tribusSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SousFamille query and add missing value', () => {
      const tribu: ITribu = { id: 456 };
      const sousFamille: ISousFamille = { id: 84098 };
      tribu.sousFamille = sousFamille;

      const sousFamilleCollection: ISousFamille[] = [{ id: 79042 }];
      jest.spyOn(sousFamilleService, 'query').mockReturnValue(of(new HttpResponse({ body: sousFamilleCollection })));
      const additionalSousFamilles = [sousFamille];
      const expectedCollection: ISousFamille[] = [...additionalSousFamilles, ...sousFamilleCollection];
      jest.spyOn(sousFamilleService, 'addSousFamilleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tribu });
      comp.ngOnInit();

      expect(sousFamilleService.query).toHaveBeenCalled();
      expect(sousFamilleService.addSousFamilleToCollectionIfMissing).toHaveBeenCalledWith(sousFamilleCollection, ...additionalSousFamilles);
      expect(comp.sousFamillesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tribu: ITribu = { id: 456 };
      const tribu: ITribu = { id: 50596 };
      tribu.tribu = tribu;
      const sousFamille: ISousFamille = { id: 77413 };
      tribu.sousFamille = sousFamille;

      activatedRoute.data = of({ tribu });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tribu));
      expect(comp.tribusSharedCollection).toContain(tribu);
      expect(comp.sousFamillesSharedCollection).toContain(sousFamille);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Tribu>>();
      const tribu = { id: 123 };
      jest.spyOn(tribuService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tribu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tribu }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tribuService.update).toHaveBeenCalledWith(tribu);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Tribu>>();
      const tribu = new Tribu();
      jest.spyOn(tribuService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tribu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tribu }));
      saveSubject.complete();

      // THEN
      expect(tribuService.create).toHaveBeenCalledWith(tribu);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Tribu>>();
      const tribu = { id: 123 };
      jest.spyOn(tribuService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tribu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tribuService.update).toHaveBeenCalledWith(tribu);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackTribuById', () => {
      it('Should return tracked Tribu primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTribuById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSousFamilleById', () => {
      it('Should return tracked SousFamille primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousFamilleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
