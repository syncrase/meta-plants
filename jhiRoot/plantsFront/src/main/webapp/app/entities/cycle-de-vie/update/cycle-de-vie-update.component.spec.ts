jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CycleDeVieService } from '../service/cycle-de-vie.service';
import { ICycleDeVie, CycleDeVie } from '../cycle-de-vie.model';
import { ISemis } from 'app/entities/semis/semis.model';
import { SemisService } from 'app/entities/semis/service/semis.service';
import { IPeriodeAnnee } from 'app/entities/periode-annee/periode-annee.model';
import { PeriodeAnneeService } from 'app/entities/periode-annee/service/periode-annee.service';
import { IReproduction } from 'app/entities/reproduction/reproduction.model';
import { ReproductionService } from 'app/entities/reproduction/service/reproduction.service';

import { CycleDeVieUpdateComponent } from './cycle-de-vie-update.component';

describe('CycleDeVie Management Update Component', () => {
  let comp: CycleDeVieUpdateComponent;
  let fixture: ComponentFixture<CycleDeVieUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cycleDeVieService: CycleDeVieService;
  let semisService: SemisService;
  let periodeAnneeService: PeriodeAnneeService;
  let reproductionService: ReproductionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CycleDeVieUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CycleDeVieUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CycleDeVieUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cycleDeVieService = TestBed.inject(CycleDeVieService);
    semisService = TestBed.inject(SemisService);
    periodeAnneeService = TestBed.inject(PeriodeAnneeService);
    reproductionService = TestBed.inject(ReproductionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call semis query and add missing value', () => {
      const cycleDeVie: ICycleDeVie = { id: 456 };
      const semis: ISemis = { id: 85629 };
      cycleDeVie.semis = semis;

      const semisCollection: ISemis[] = [{ id: 97375 }];
      jest.spyOn(semisService, 'query').mockReturnValue(of(new HttpResponse({ body: semisCollection })));
      const expectedCollection: ISemis[] = [semis, ...semisCollection];
      jest.spyOn(semisService, 'addSemisToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cycleDeVie });
      comp.ngOnInit();

      expect(semisService.query).toHaveBeenCalled();
      expect(semisService.addSemisToCollectionIfMissing).toHaveBeenCalledWith(semisCollection, semis);
      expect(comp.semisCollection).toEqual(expectedCollection);
    });

    it('Should call apparitionFeuilles query and add missing value', () => {
      const cycleDeVie: ICycleDeVie = { id: 456 };
      const apparitionFeuilles: IPeriodeAnnee = { id: 96705 };
      cycleDeVie.apparitionFeuilles = apparitionFeuilles;

      const apparitionFeuillesCollection: IPeriodeAnnee[] = [{ id: 61793 }];
      jest.spyOn(periodeAnneeService, 'query').mockReturnValue(of(new HttpResponse({ body: apparitionFeuillesCollection })));
      const expectedCollection: IPeriodeAnnee[] = [apparitionFeuilles, ...apparitionFeuillesCollection];
      jest.spyOn(periodeAnneeService, 'addPeriodeAnneeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cycleDeVie });
      comp.ngOnInit();

      expect(periodeAnneeService.query).toHaveBeenCalled();
      expect(periodeAnneeService.addPeriodeAnneeToCollectionIfMissing).toHaveBeenCalledWith(
        apparitionFeuillesCollection,
        apparitionFeuilles
      );
      expect(comp.apparitionFeuillesCollection).toEqual(expectedCollection);
    });

    it('Should call floraison query and add missing value', () => {
      const cycleDeVie: ICycleDeVie = { id: 456 };
      const floraison: IPeriodeAnnee = { id: 80100 };
      cycleDeVie.floraison = floraison;

      const floraisonCollection: IPeriodeAnnee[] = [{ id: 95026 }];
      jest.spyOn(periodeAnneeService, 'query').mockReturnValue(of(new HttpResponse({ body: floraisonCollection })));
      const expectedCollection: IPeriodeAnnee[] = [floraison, ...floraisonCollection];
      jest.spyOn(periodeAnneeService, 'addPeriodeAnneeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cycleDeVie });
      comp.ngOnInit();

      expect(periodeAnneeService.query).toHaveBeenCalled();
      expect(periodeAnneeService.addPeriodeAnneeToCollectionIfMissing).toHaveBeenCalledWith(floraisonCollection, floraison);
      expect(comp.floraisonsCollection).toEqual(expectedCollection);
    });

    it('Should call recolte query and add missing value', () => {
      const cycleDeVie: ICycleDeVie = { id: 456 };
      const recolte: IPeriodeAnnee = { id: 9241 };
      cycleDeVie.recolte = recolte;

      const recolteCollection: IPeriodeAnnee[] = [{ id: 11474 }];
      jest.spyOn(periodeAnneeService, 'query').mockReturnValue(of(new HttpResponse({ body: recolteCollection })));
      const expectedCollection: IPeriodeAnnee[] = [recolte, ...recolteCollection];
      jest.spyOn(periodeAnneeService, 'addPeriodeAnneeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cycleDeVie });
      comp.ngOnInit();

      expect(periodeAnneeService.query).toHaveBeenCalled();
      expect(periodeAnneeService.addPeriodeAnneeToCollectionIfMissing).toHaveBeenCalledWith(recolteCollection, recolte);
      expect(comp.recoltesCollection).toEqual(expectedCollection);
    });

    it('Should call croissance query and add missing value', () => {
      const cycleDeVie: ICycleDeVie = { id: 456 };
      const croissance: IPeriodeAnnee = { id: 47849 };
      cycleDeVie.croissance = croissance;

      const croissanceCollection: IPeriodeAnnee[] = [{ id: 44847 }];
      jest.spyOn(periodeAnneeService, 'query').mockReturnValue(of(new HttpResponse({ body: croissanceCollection })));
      const expectedCollection: IPeriodeAnnee[] = [croissance, ...croissanceCollection];
      jest.spyOn(periodeAnneeService, 'addPeriodeAnneeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cycleDeVie });
      comp.ngOnInit();

      expect(periodeAnneeService.query).toHaveBeenCalled();
      expect(periodeAnneeService.addPeriodeAnneeToCollectionIfMissing).toHaveBeenCalledWith(croissanceCollection, croissance);
      expect(comp.croissancesCollection).toEqual(expectedCollection);
    });

    it('Should call maturite query and add missing value', () => {
      const cycleDeVie: ICycleDeVie = { id: 456 };
      const maturite: IPeriodeAnnee = { id: 27616 };
      cycleDeVie.maturite = maturite;

      const maturiteCollection: IPeriodeAnnee[] = [{ id: 49249 }];
      jest.spyOn(periodeAnneeService, 'query').mockReturnValue(of(new HttpResponse({ body: maturiteCollection })));
      const expectedCollection: IPeriodeAnnee[] = [maturite, ...maturiteCollection];
      jest.spyOn(periodeAnneeService, 'addPeriodeAnneeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cycleDeVie });
      comp.ngOnInit();

      expect(periodeAnneeService.query).toHaveBeenCalled();
      expect(periodeAnneeService.addPeriodeAnneeToCollectionIfMissing).toHaveBeenCalledWith(maturiteCollection, maturite);
      expect(comp.maturitesCollection).toEqual(expectedCollection);
    });

    it('Should call plantation query and add missing value', () => {
      const cycleDeVie: ICycleDeVie = { id: 456 };
      const plantation: IPeriodeAnnee = { id: 21279 };
      cycleDeVie.plantation = plantation;

      const plantationCollection: IPeriodeAnnee[] = [{ id: 33027 }];
      jest.spyOn(periodeAnneeService, 'query').mockReturnValue(of(new HttpResponse({ body: plantationCollection })));
      const expectedCollection: IPeriodeAnnee[] = [plantation, ...plantationCollection];
      jest.spyOn(periodeAnneeService, 'addPeriodeAnneeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cycleDeVie });
      comp.ngOnInit();

      expect(periodeAnneeService.query).toHaveBeenCalled();
      expect(periodeAnneeService.addPeriodeAnneeToCollectionIfMissing).toHaveBeenCalledWith(plantationCollection, plantation);
      expect(comp.plantationsCollection).toEqual(expectedCollection);
    });

    it('Should call rempotage query and add missing value', () => {
      const cycleDeVie: ICycleDeVie = { id: 456 };
      const rempotage: IPeriodeAnnee = { id: 13078 };
      cycleDeVie.rempotage = rempotage;

      const rempotageCollection: IPeriodeAnnee[] = [{ id: 78499 }];
      jest.spyOn(periodeAnneeService, 'query').mockReturnValue(of(new HttpResponse({ body: rempotageCollection })));
      const expectedCollection: IPeriodeAnnee[] = [rempotage, ...rempotageCollection];
      jest.spyOn(periodeAnneeService, 'addPeriodeAnneeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cycleDeVie });
      comp.ngOnInit();

      expect(periodeAnneeService.query).toHaveBeenCalled();
      expect(periodeAnneeService.addPeriodeAnneeToCollectionIfMissing).toHaveBeenCalledWith(rempotageCollection, rempotage);
      expect(comp.rempotagesCollection).toEqual(expectedCollection);
    });

    it('Should call Reproduction query and add missing value', () => {
      const cycleDeVie: ICycleDeVie = { id: 456 };
      const reproduction: IReproduction = { id: 4068 };
      cycleDeVie.reproduction = reproduction;

      const reproductionCollection: IReproduction[] = [{ id: 29511 }];
      jest.spyOn(reproductionService, 'query').mockReturnValue(of(new HttpResponse({ body: reproductionCollection })));
      const additionalReproductions = [reproduction];
      const expectedCollection: IReproduction[] = [...additionalReproductions, ...reproductionCollection];
      jest.spyOn(reproductionService, 'addReproductionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cycleDeVie });
      comp.ngOnInit();

      expect(reproductionService.query).toHaveBeenCalled();
      expect(reproductionService.addReproductionToCollectionIfMissing).toHaveBeenCalledWith(
        reproductionCollection,
        ...additionalReproductions
      );
      expect(comp.reproductionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cycleDeVie: ICycleDeVie = { id: 456 };
      const semis: ISemis = { id: 50141 };
      cycleDeVie.semis = semis;
      const apparitionFeuilles: IPeriodeAnnee = { id: 32706 };
      cycleDeVie.apparitionFeuilles = apparitionFeuilles;
      const floraison: IPeriodeAnnee = { id: 87461 };
      cycleDeVie.floraison = floraison;
      const recolte: IPeriodeAnnee = { id: 3465 };
      cycleDeVie.recolte = recolte;
      const croissance: IPeriodeAnnee = { id: 6667 };
      cycleDeVie.croissance = croissance;
      const maturite: IPeriodeAnnee = { id: 84329 };
      cycleDeVie.maturite = maturite;
      const plantation: IPeriodeAnnee = { id: 11086 };
      cycleDeVie.plantation = plantation;
      const rempotage: IPeriodeAnnee = { id: 60787 };
      cycleDeVie.rempotage = rempotage;
      const reproduction: IReproduction = { id: 5780 };
      cycleDeVie.reproduction = reproduction;

      activatedRoute.data = of({ cycleDeVie });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cycleDeVie));
      expect(comp.semisCollection).toContain(semis);
      expect(comp.apparitionFeuillesCollection).toContain(apparitionFeuilles);
      expect(comp.floraisonsCollection).toContain(floraison);
      expect(comp.recoltesCollection).toContain(recolte);
      expect(comp.croissancesCollection).toContain(croissance);
      expect(comp.maturitesCollection).toContain(maturite);
      expect(comp.plantationsCollection).toContain(plantation);
      expect(comp.rempotagesCollection).toContain(rempotage);
      expect(comp.reproductionsSharedCollection).toContain(reproduction);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CycleDeVie>>();
      const cycleDeVie = { id: 123 };
      jest.spyOn(cycleDeVieService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cycleDeVie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cycleDeVie }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cycleDeVieService.update).toHaveBeenCalledWith(cycleDeVie);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CycleDeVie>>();
      const cycleDeVie = new CycleDeVie();
      jest.spyOn(cycleDeVieService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cycleDeVie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cycleDeVie }));
      saveSubject.complete();

      // THEN
      expect(cycleDeVieService.create).toHaveBeenCalledWith(cycleDeVie);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CycleDeVie>>();
      const cycleDeVie = { id: 123 };
      jest.spyOn(cycleDeVieService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cycleDeVie });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cycleDeVieService.update).toHaveBeenCalledWith(cycleDeVie);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSemisById', () => {
      it('Should return tracked Semis primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSemisById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackPeriodeAnneeById', () => {
      it('Should return tracked PeriodeAnnee primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackPeriodeAnneeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackReproductionById', () => {
      it('Should return tracked Reproduction primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackReproductionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
