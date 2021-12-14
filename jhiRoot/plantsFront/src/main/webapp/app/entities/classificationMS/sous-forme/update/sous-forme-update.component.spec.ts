jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SousFormeService } from '../service/sous-forme.service';
import { ISousForme, SousForme } from '../sous-forme.model';
import { IForme } from 'app/entities/classificationMS/forme/forme.model';
import { FormeService } from 'app/entities/classificationMS/forme/service/forme.service';

import { SousFormeUpdateComponent } from './sous-forme-update.component';

describe('SousForme Management Update Component', () => {
  let comp: SousFormeUpdateComponent;
  let fixture: ComponentFixture<SousFormeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sousFormeService: SousFormeService;
  let formeService: FormeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SousFormeUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SousFormeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SousFormeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sousFormeService = TestBed.inject(SousFormeService);
    formeService = TestBed.inject(FormeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SousForme query and add missing value', () => {
      const sousForme: ISousForme = { id: 456 };
      const sousForme: ISousForme = { id: 23723 };
      sousForme.sousForme = sousForme;

      const sousFormeCollection: ISousForme[] = [{ id: 45079 }];
      jest.spyOn(sousFormeService, 'query').mockReturnValue(of(new HttpResponse({ body: sousFormeCollection })));
      const additionalSousFormes = [sousForme];
      const expectedCollection: ISousForme[] = [...additionalSousFormes, ...sousFormeCollection];
      jest.spyOn(sousFormeService, 'addSousFormeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousForme });
      comp.ngOnInit();

      expect(sousFormeService.query).toHaveBeenCalled();
      expect(sousFormeService.addSousFormeToCollectionIfMissing).toHaveBeenCalledWith(sousFormeCollection, ...additionalSousFormes);
      expect(comp.sousFormesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Forme query and add missing value', () => {
      const sousForme: ISousForme = { id: 456 };
      const forme: IForme = { id: 22482 };
      sousForme.forme = forme;

      const formeCollection: IForme[] = [{ id: 1946 }];
      jest.spyOn(formeService, 'query').mockReturnValue(of(new HttpResponse({ body: formeCollection })));
      const additionalFormes = [forme];
      const expectedCollection: IForme[] = [...additionalFormes, ...formeCollection];
      jest.spyOn(formeService, 'addFormeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousForme });
      comp.ngOnInit();

      expect(formeService.query).toHaveBeenCalled();
      expect(formeService.addFormeToCollectionIfMissing).toHaveBeenCalledWith(formeCollection, ...additionalFormes);
      expect(comp.formesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sousForme: ISousForme = { id: 456 };
      const sousForme: ISousForme = { id: 14325 };
      sousForme.sousForme = sousForme;
      const forme: IForme = { id: 95098 };
      sousForme.forme = forme;

      activatedRoute.data = of({ sousForme });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sousForme));
      expect(comp.sousFormesSharedCollection).toContain(sousForme);
      expect(comp.formesSharedCollection).toContain(forme);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousForme>>();
      const sousForme = { id: 123 };
      jest.spyOn(sousFormeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousForme });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousForme }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sousFormeService.update).toHaveBeenCalledWith(sousForme);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousForme>>();
      const sousForme = new SousForme();
      jest.spyOn(sousFormeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousForme });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousForme }));
      saveSubject.complete();

      // THEN
      expect(sousFormeService.create).toHaveBeenCalledWith(sousForme);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousForme>>();
      const sousForme = { id: 123 };
      jest.spyOn(sousFormeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousForme });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sousFormeService.update).toHaveBeenCalledWith(sousForme);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSousFormeById', () => {
      it('Should return tracked SousForme primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousFormeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFormeById', () => {
      it('Should return tracked Forme primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFormeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
