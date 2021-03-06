jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CronquistRankService } from '../service/cronquist-rank.service';
import { ICronquistRank, CronquistRank } from '../cronquist-rank.model';

import { CronquistRankUpdateComponent } from './cronquist-rank-update.component';

describe('CronquistRank Management Update Component', () => {
  let comp: CronquistRankUpdateComponent;
  let fixture: ComponentFixture<CronquistRankUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cronquistRankService: CronquistRankService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CronquistRankUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CronquistRankUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CronquistRankUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cronquistRankService = TestBed.inject(CronquistRankService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CronquistRank query and add missing value', () => {
      const cronquistRank: ICronquistRank = { id: 456 };
      const parent: ICronquistRank = { id: 7986 };
      cronquistRank.parent = parent;

      const cronquistRankCollection: ICronquistRank[] = [{ id: 38058 }];
      jest.spyOn(cronquistRankService, 'query').mockReturnValue(of(new HttpResponse({ body: cronquistRankCollection })));
      const additionalCronquistRanks = [parent];
      const expectedCollection: ICronquistRank[] = [...additionalCronquistRanks, ...cronquistRankCollection];
      jest.spyOn(cronquistRankService, 'addCronquistRankToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cronquistRank });
      comp.ngOnInit();

      expect(cronquistRankService.query).toHaveBeenCalled();
      expect(cronquistRankService.addCronquistRankToCollectionIfMissing).toHaveBeenCalledWith(
        cronquistRankCollection,
        ...additionalCronquistRanks
      );
      expect(comp.cronquistRanksSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cronquistRank: ICronquistRank = { id: 456 };
      const parent: ICronquistRank = { id: 96515 };
      cronquistRank.parent = parent;

      activatedRoute.data = of({ cronquistRank });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cronquistRank));
      expect(comp.cronquistRanksSharedCollection).toContain(parent);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CronquistRank>>();
      const cronquistRank = { id: 123 };
      jest.spyOn(cronquistRankService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cronquistRank });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cronquistRank }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cronquistRankService.update).toHaveBeenCalledWith(cronquistRank);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CronquistRank>>();
      const cronquistRank = new CronquistRank();
      jest.spyOn(cronquistRankService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cronquistRank });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cronquistRank }));
      saveSubject.complete();

      // THEN
      expect(cronquistRankService.create).toHaveBeenCalledWith(cronquistRank);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CronquistRank>>();
      const cronquistRank = { id: 123 };
      jest.spyOn(cronquistRankService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cronquistRank });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cronquistRankService.update).toHaveBeenCalledWith(cronquistRank);
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
