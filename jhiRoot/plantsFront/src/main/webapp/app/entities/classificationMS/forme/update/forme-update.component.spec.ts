jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FormeService } from '../service/forme.service';
import { IForme, Forme } from '../forme.model';
import { ISousVariete } from 'app/entities/classificationMS/sous-variete/sous-variete.model';
import { SousVarieteService } from 'app/entities/classificationMS/sous-variete/service/sous-variete.service';

import { FormeUpdateComponent } from './forme-update.component';

describe('Forme Management Update Component', () => {
  let comp: FormeUpdateComponent;
  let fixture: ComponentFixture<FormeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let formeService: FormeService;
  let sousVarieteService: SousVarieteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [FormeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(FormeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FormeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    formeService = TestBed.inject(FormeService);
    sousVarieteService = TestBed.inject(SousVarieteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Forme query and add missing value', () => {
      const forme: IForme = { id: 456 };
      const forme: IForme = { id: 50426 };
      forme.forme = forme;

      const formeCollection: IForme[] = [{ id: 92154 }];
      jest.spyOn(formeService, 'query').mockReturnValue(of(new HttpResponse({ body: formeCollection })));
      const additionalFormes = [forme];
      const expectedCollection: IForme[] = [...additionalFormes, ...formeCollection];
      jest.spyOn(formeService, 'addFormeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ forme });
      comp.ngOnInit();

      expect(formeService.query).toHaveBeenCalled();
      expect(formeService.addFormeToCollectionIfMissing).toHaveBeenCalledWith(formeCollection, ...additionalFormes);
      expect(comp.formesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SousVariete query and add missing value', () => {
      const forme: IForme = { id: 456 };
      const sousVariete: ISousVariete = { id: 62131 };
      forme.sousVariete = sousVariete;

      const sousVarieteCollection: ISousVariete[] = [{ id: 51915 }];
      jest.spyOn(sousVarieteService, 'query').mockReturnValue(of(new HttpResponse({ body: sousVarieteCollection })));
      const additionalSousVarietes = [sousVariete];
      const expectedCollection: ISousVariete[] = [...additionalSousVarietes, ...sousVarieteCollection];
      jest.spyOn(sousVarieteService, 'addSousVarieteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ forme });
      comp.ngOnInit();

      expect(sousVarieteService.query).toHaveBeenCalled();
      expect(sousVarieteService.addSousVarieteToCollectionIfMissing).toHaveBeenCalledWith(sousVarieteCollection, ...additionalSousVarietes);
      expect(comp.sousVarietesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const forme: IForme = { id: 456 };
      const forme: IForme = { id: 27867 };
      forme.forme = forme;
      const sousVariete: ISousVariete = { id: 42821 };
      forme.sousVariete = sousVariete;

      activatedRoute.data = of({ forme });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(forme));
      expect(comp.formesSharedCollection).toContain(forme);
      expect(comp.sousVarietesSharedCollection).toContain(sousVariete);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Forme>>();
      const forme = { id: 123 };
      jest.spyOn(formeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ forme });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: forme }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(formeService.update).toHaveBeenCalledWith(forme);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Forme>>();
      const forme = new Forme();
      jest.spyOn(formeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ forme });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: forme }));
      saveSubject.complete();

      // THEN
      expect(formeService.create).toHaveBeenCalledWith(forme);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Forme>>();
      const forme = { id: 123 };
      jest.spyOn(formeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ forme });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(formeService.update).toHaveBeenCalledWith(forme);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFormeById', () => {
      it('Should return tracked Forme primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFormeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSousVarieteById', () => {
      it('Should return tracked SousVariete primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousVarieteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
