jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SousClasseService } from '../service/sous-classe.service';
import { ISousClasse, SousClasse } from '../sous-classe.model';
import { IClasse } from 'app/entities/classificationMS/classe/classe.model';
import { ClasseService } from 'app/entities/classificationMS/classe/service/classe.service';

import { SousClasseUpdateComponent } from './sous-classe-update.component';

describe('SousClasse Management Update Component', () => {
  let comp: SousClasseUpdateComponent;
  let fixture: ComponentFixture<SousClasseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sousClasseService: SousClasseService;
  let classeService: ClasseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [SousClasseUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(SousClasseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SousClasseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sousClasseService = TestBed.inject(SousClasseService);
    classeService = TestBed.inject(ClasseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SousClasse query and add missing value', () => {
      const sousClasse: ISousClasse = { id: 456 };
      const sousClasse: ISousClasse = { id: 14113 };
      sousClasse.sousClasse = sousClasse;

      const sousClasseCollection: ISousClasse[] = [{ id: 55233 }];
      jest.spyOn(sousClasseService, 'query').mockReturnValue(of(new HttpResponse({ body: sousClasseCollection })));
      const additionalSousClasses = [sousClasse];
      const expectedCollection: ISousClasse[] = [...additionalSousClasses, ...sousClasseCollection];
      jest.spyOn(sousClasseService, 'addSousClasseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousClasse });
      comp.ngOnInit();

      expect(sousClasseService.query).toHaveBeenCalled();
      expect(sousClasseService.addSousClasseToCollectionIfMissing).toHaveBeenCalledWith(sousClasseCollection, ...additionalSousClasses);
      expect(comp.sousClassesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Classe query and add missing value', () => {
      const sousClasse: ISousClasse = { id: 456 };
      const classe: IClasse = { id: 25322 };
      sousClasse.classe = classe;

      const classeCollection: IClasse[] = [{ id: 86304 }];
      jest.spyOn(classeService, 'query').mockReturnValue(of(new HttpResponse({ body: classeCollection })));
      const additionalClasses = [classe];
      const expectedCollection: IClasse[] = [...additionalClasses, ...classeCollection];
      jest.spyOn(classeService, 'addClasseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sousClasse });
      comp.ngOnInit();

      expect(classeService.query).toHaveBeenCalled();
      expect(classeService.addClasseToCollectionIfMissing).toHaveBeenCalledWith(classeCollection, ...additionalClasses);
      expect(comp.classesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sousClasse: ISousClasse = { id: 456 };
      const sousClasse: ISousClasse = { id: 11460 };
      sousClasse.sousClasse = sousClasse;
      const classe: IClasse = { id: 68436 };
      sousClasse.classe = classe;

      activatedRoute.data = of({ sousClasse });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(sousClasse));
      expect(comp.sousClassesSharedCollection).toContain(sousClasse);
      expect(comp.classesSharedCollection).toContain(classe);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousClasse>>();
      const sousClasse = { id: 123 };
      jest.spyOn(sousClasseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousClasse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousClasse }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(sousClasseService.update).toHaveBeenCalledWith(sousClasse);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousClasse>>();
      const sousClasse = new SousClasse();
      jest.spyOn(sousClasseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousClasse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousClasse }));
      saveSubject.complete();

      // THEN
      expect(sousClasseService.create).toHaveBeenCalledWith(sousClasse);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<SousClasse>>();
      const sousClasse = { id: 123 };
      jest.spyOn(sousClasseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousClasse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sousClasseService.update).toHaveBeenCalledWith(sousClasse);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSousClasseById', () => {
      it('Should return tracked SousClasse primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSousClasseById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackClasseById', () => {
      it('Should return tracked Classe primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClasseById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
