jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClasseService } from '../service/classe.service';
import { IClasse, Classe } from '../classe.model';
import { ISuperClasse } from 'app/entities/classificationMS/super-classe/super-classe.model';
import { SuperClasseService } from 'app/entities/classificationMS/super-classe/service/super-classe.service';

import { ClasseUpdateComponent } from './classe-update.component';

describe('Classe Management Update Component', () => {
  let comp: ClasseUpdateComponent;
  let fixture: ComponentFixture<ClasseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let classeService: ClasseService;
  let superClasseService: SuperClasseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ClasseUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ClasseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClasseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    classeService = TestBed.inject(ClasseService);
    superClasseService = TestBed.inject(SuperClasseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Classe query and add missing value', () => {
      const classe: IClasse = { id: 456 };
      const classe: IClasse = { id: 51256 };
      classe.classe = classe;

      const classeCollection: IClasse[] = [{ id: 15238 }];
      jest.spyOn(classeService, 'query').mockReturnValue(of(new HttpResponse({ body: classeCollection })));
      const additionalClasses = [classe];
      const expectedCollection: IClasse[] = [...additionalClasses, ...classeCollection];
      jest.spyOn(classeService, 'addClasseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      expect(classeService.query).toHaveBeenCalled();
      expect(classeService.addClasseToCollectionIfMissing).toHaveBeenCalledWith(classeCollection, ...additionalClasses);
      expect(comp.classesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SuperClasse query and add missing value', () => {
      const classe: IClasse = { id: 456 };
      const superClasse: ISuperClasse = { id: 49557 };
      classe.superClasse = superClasse;

      const superClasseCollection: ISuperClasse[] = [{ id: 84929 }];
      jest.spyOn(superClasseService, 'query').mockReturnValue(of(new HttpResponse({ body: superClasseCollection })));
      const additionalSuperClasses = [superClasse];
      const expectedCollection: ISuperClasse[] = [...additionalSuperClasses, ...superClasseCollection];
      jest.spyOn(superClasseService, 'addSuperClasseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      expect(superClasseService.query).toHaveBeenCalled();
      expect(superClasseService.addSuperClasseToCollectionIfMissing).toHaveBeenCalledWith(superClasseCollection, ...additionalSuperClasses);
      expect(comp.superClassesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const classe: IClasse = { id: 456 };
      const classe: IClasse = { id: 98529 };
      classe.classe = classe;
      const superClasse: ISuperClasse = { id: 21339 };
      classe.superClasse = superClasse;

      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(classe));
      expect(comp.classesSharedCollection).toContain(classe);
      expect(comp.superClassesSharedCollection).toContain(superClasse);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Classe>>();
      const classe = { id: 123 };
      jest.spyOn(classeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classe }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(classeService.update).toHaveBeenCalledWith(classe);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Classe>>();
      const classe = new Classe();
      jest.spyOn(classeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classe }));
      saveSubject.complete();

      // THEN
      expect(classeService.create).toHaveBeenCalledWith(classe);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Classe>>();
      const classe = { id: 123 };
      jest.spyOn(classeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(classeService.update).toHaveBeenCalledWith(classe);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackClasseById', () => {
      it('Should return tracked Classe primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackClasseById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackSuperClasseById', () => {
      it('Should return tracked SuperClasse primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSuperClasseById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
