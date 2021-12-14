jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { APGIIIPlanteService } from '../service/apgiii-plante.service';
import { IAPGIIIPlante, APGIIIPlante } from '../apgiii-plante.model';
import { IClade } from 'app/entities/plantsMS/clade/clade.model';
import { CladeService } from 'app/entities/plantsMS/clade/service/clade.service';

import { APGIIIPlanteUpdateComponent } from './apgiii-plante-update.component';

describe('APGIIIPlante Management Update Component', () => {
  let comp: APGIIIPlanteUpdateComponent;
  let fixture: ComponentFixture<APGIIIPlanteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let aPGIIIPlanteService: APGIIIPlanteService;
  let cladeService: CladeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [APGIIIPlanteUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(APGIIIPlanteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(APGIIIPlanteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    aPGIIIPlanteService = TestBed.inject(APGIIIPlanteService);
    cladeService = TestBed.inject(CladeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Clade query and add missing value', () => {
      const aPGIIIPlante: IAPGIIIPlante = { id: 456 };
      const clades: IClade[] = [{ id: 70857 }];
      aPGIIIPlante.clades = clades;

      const cladeCollection: IClade[] = [{ id: 41726 }];
      jest.spyOn(cladeService, 'query').mockReturnValue(of(new HttpResponse({ body: cladeCollection })));
      const additionalClades = [...clades];
      const expectedCollection: IClade[] = [...additionalClades, ...cladeCollection];
      jest.spyOn(cladeService, 'addCladeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ aPGIIIPlante });
      comp.ngOnInit();

      expect(cladeService.query).toHaveBeenCalled();
      expect(cladeService.addCladeToCollectionIfMissing).toHaveBeenCalledWith(cladeCollection, ...additionalClades);
      expect(comp.cladesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const aPGIIIPlante: IAPGIIIPlante = { id: 456 };
      const clades: IClade = { id: 52407 };
      aPGIIIPlante.clades = [clades];

      activatedRoute.data = of({ aPGIIIPlante });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(aPGIIIPlante));
      expect(comp.cladesSharedCollection).toContain(clades);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGIIIPlante>>();
      const aPGIIIPlante = { id: 123 };
      jest.spyOn(aPGIIIPlanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGIIIPlante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aPGIIIPlante }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(aPGIIIPlanteService.update).toHaveBeenCalledWith(aPGIIIPlante);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGIIIPlante>>();
      const aPGIIIPlante = new APGIIIPlante();
      jest.spyOn(aPGIIIPlanteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGIIIPlante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: aPGIIIPlante }));
      saveSubject.complete();

      // THEN
      expect(aPGIIIPlanteService.create).toHaveBeenCalledWith(aPGIIIPlante);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<APGIIIPlante>>();
      const aPGIIIPlante = { id: 123 };
      jest.spyOn(aPGIIIPlanteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ aPGIIIPlante });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(aPGIIIPlanteService.update).toHaveBeenCalledWith(aPGIIIPlante);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCladeById', () => {
      it('Should return tracked Clade primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCladeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedClade', () => {
      it('Should return option if no Clade is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedClade(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Clade for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedClade(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Clade is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedClade(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
