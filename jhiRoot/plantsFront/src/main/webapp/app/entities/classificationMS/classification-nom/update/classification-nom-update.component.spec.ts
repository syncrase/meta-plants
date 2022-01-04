jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClassificationNomService } from '../service/classification-nom.service';
import { IClassificationNom, ClassificationNom } from '../classification-nom.model';
import { ICronquistRank } from 'app/entities/classificationMS/cronquist-rank/cronquist-rank.model';
import { CronquistRankService } from 'app/entities/classificationMS/cronquist-rank/service/cronquist-rank.service';

import { ClassificationNomUpdateComponent } from './classification-nom-update.component';

describe('ClassificationNom Management Update Component', () => {
  let comp: ClassificationNomUpdateComponent;
  let fixture: ComponentFixture<ClassificationNomUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let classificationNomService: ClassificationNomService;
  let cronquistRankService: CronquistRankService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ClassificationNomUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ClassificationNomUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClassificationNomUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    classificationNomService = TestBed.inject(ClassificationNomService);
    cronquistRankService = TestBed.inject(CronquistRankService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CronquistRank query and add missing value', () => {
      const classificationNom: IClassificationNom = { id: 456 };
      const cronquistRank: ICronquistRank = { id: 91956 };
      classificationNom.cronquistRank = cronquistRank;

      const cronquistRankCollection: ICronquistRank[] = [{ id: 63757 }];
      jest.spyOn(cronquistRankService, 'query').mockReturnValue(of(new HttpResponse({ body: cronquistRankCollection })));
      const additionalCronquistRanks = [cronquistRank];
      const expectedCollection: ICronquistRank[] = [...additionalCronquistRanks, ...cronquistRankCollection];
      jest.spyOn(cronquistRankService, 'addCronquistRankToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classificationNom });
      comp.ngOnInit();

      expect(cronquistRankService.query).toHaveBeenCalled();
      expect(cronquistRankService.addCronquistRankToCollectionIfMissing).toHaveBeenCalledWith(
        cronquistRankCollection,
        ...additionalCronquistRanks
      );
      expect(comp.cronquistRanksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const classificationNom: IClassificationNom = { id: 456 };
      const cronquistRank: ICronquistRank = { id: 94342 };
      classificationNom.cronquistRank = cronquistRank;

      activatedRoute.data = of({ classificationNom });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(classificationNom));
      expect(comp.cronquistRanksSharedCollection).toContain(cronquistRank);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ClassificationNom>>();
      const classificationNom = { id: 123 };
      jest.spyOn(classificationNomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classificationNom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classificationNom }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(classificationNomService.update).toHaveBeenCalledWith(classificationNom);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ClassificationNom>>();
      const classificationNom = new ClassificationNom();
      jest.spyOn(classificationNomService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classificationNom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classificationNom }));
      saveSubject.complete();

      // THEN
      expect(classificationNomService.create).toHaveBeenCalledWith(classificationNom);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ClassificationNom>>();
      const classificationNom = { id: 123 };
      jest.spyOn(classificationNomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classificationNom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(classificationNomService.update).toHaveBeenCalledWith(classificationNom);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCronquistRankById', () => {
      it('Should return tracked CronquistRank primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCronquistRankById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
