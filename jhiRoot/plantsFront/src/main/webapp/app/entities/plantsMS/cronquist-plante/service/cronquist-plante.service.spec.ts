import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICronquistPlante, CronquistPlante } from '../cronquist-plante.model';

import { CronquistPlanteService } from './cronquist-plante.service';

describe('CronquistPlante Service', () => {
  let service: CronquistPlanteService;
  let httpMock: HttpTestingController;
  let elemDefault: ICronquistPlante;
  let expectedResult: ICronquistPlante | ICronquistPlante[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CronquistPlanteService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      superRegne: 'AAAAAAA',
      regne: 'AAAAAAA',
      sousRegne: 'AAAAAAA',
      rameau: 'AAAAAAA',
      infraRegne: 'AAAAAAA',
      superDivision: 'AAAAAAA',
      division: 'AAAAAAA',
      sousDivision: 'AAAAAAA',
      infraEmbranchement: 'AAAAAAA',
      microEmbranchement: 'AAAAAAA',
      superClasse: 'AAAAAAA',
      classe: 'AAAAAAA',
      sousClasse: 'AAAAAAA',
      infraClasse: 'AAAAAAA',
      superOrdre: 'AAAAAAA',
      ordre: 'AAAAAAA',
      sousOrdre: 'AAAAAAA',
      infraOrdre: 'AAAAAAA',
      microOrdre: 'AAAAAAA',
      superFamille: 'AAAAAAA',
      famille: 'AAAAAAA',
      sousFamille: 'AAAAAAA',
      tribu: 'AAAAAAA',
      sousTribu: 'AAAAAAA',
      genre: 'AAAAAAA',
      sousGenre: 'AAAAAAA',
      section: 'AAAAAAA',
      sousSection: 'AAAAAAA',
      espece: 'AAAAAAA',
      sousEspece: 'AAAAAAA',
      variete: 'AAAAAAA',
      sousVariete: 'AAAAAAA',
      forme: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CronquistPlante', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CronquistPlante()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CronquistPlante', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          superRegne: 'BBBBBB',
          regne: 'BBBBBB',
          sousRegne: 'BBBBBB',
          rameau: 'BBBBBB',
          infraRegne: 'BBBBBB',
          superDivision: 'BBBBBB',
          division: 'BBBBBB',
          sousDivision: 'BBBBBB',
          infraEmbranchement: 'BBBBBB',
          microEmbranchement: 'BBBBBB',
          superClasse: 'BBBBBB',
          classe: 'BBBBBB',
          sousClasse: 'BBBBBB',
          infraClasse: 'BBBBBB',
          superOrdre: 'BBBBBB',
          ordre: 'BBBBBB',
          sousOrdre: 'BBBBBB',
          infraOrdre: 'BBBBBB',
          microOrdre: 'BBBBBB',
          superFamille: 'BBBBBB',
          famille: 'BBBBBB',
          sousFamille: 'BBBBBB',
          tribu: 'BBBBBB',
          sousTribu: 'BBBBBB',
          genre: 'BBBBBB',
          sousGenre: 'BBBBBB',
          section: 'BBBBBB',
          sousSection: 'BBBBBB',
          espece: 'BBBBBB',
          sousEspece: 'BBBBBB',
          variete: 'BBBBBB',
          sousVariete: 'BBBBBB',
          forme: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CronquistPlante', () => {
      const patchObject = Object.assign(
        {
          superRegne: 'BBBBBB',
          regne: 'BBBBBB',
          sousRegne: 'BBBBBB',
          rameau: 'BBBBBB',
          superDivision: 'BBBBBB',
          division: 'BBBBBB',
          sousDivision: 'BBBBBB',
          infraEmbranchement: 'BBBBBB',
          microEmbranchement: 'BBBBBB',
          superClasse: 'BBBBBB',
          classe: 'BBBBBB',
          sousClasse: 'BBBBBB',
          superOrdre: 'BBBBBB',
          microOrdre: 'BBBBBB',
          famille: 'BBBBBB',
          tribu: 'BBBBBB',
          genre: 'BBBBBB',
          sousGenre: 'BBBBBB',
          section: 'BBBBBB',
          sousSection: 'BBBBBB',
          sousVariete: 'BBBBBB',
          forme: 'BBBBBB',
        },
        new CronquistPlante()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CronquistPlante', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          superRegne: 'BBBBBB',
          regne: 'BBBBBB',
          sousRegne: 'BBBBBB',
          rameau: 'BBBBBB',
          infraRegne: 'BBBBBB',
          superDivision: 'BBBBBB',
          division: 'BBBBBB',
          sousDivision: 'BBBBBB',
          infraEmbranchement: 'BBBBBB',
          microEmbranchement: 'BBBBBB',
          superClasse: 'BBBBBB',
          classe: 'BBBBBB',
          sousClasse: 'BBBBBB',
          infraClasse: 'BBBBBB',
          superOrdre: 'BBBBBB',
          ordre: 'BBBBBB',
          sousOrdre: 'BBBBBB',
          infraOrdre: 'BBBBBB',
          microOrdre: 'BBBBBB',
          superFamille: 'BBBBBB',
          famille: 'BBBBBB',
          sousFamille: 'BBBBBB',
          tribu: 'BBBBBB',
          sousTribu: 'BBBBBB',
          genre: 'BBBBBB',
          sousGenre: 'BBBBBB',
          section: 'BBBBBB',
          sousSection: 'BBBBBB',
          espece: 'BBBBBB',
          sousEspece: 'BBBBBB',
          variete: 'BBBBBB',
          sousVariete: 'BBBBBB',
          forme: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CronquistPlante', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCronquistPlanteToCollectionIfMissing', () => {
      it('should add a CronquistPlante to an empty array', () => {
        const cronquistPlante: ICronquistPlante = { id: 123 };
        expectedResult = service.addCronquistPlanteToCollectionIfMissing([], cronquistPlante);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cronquistPlante);
      });

      it('should not add a CronquistPlante to an array that contains it', () => {
        const cronquistPlante: ICronquistPlante = { id: 123 };
        const cronquistPlanteCollection: ICronquistPlante[] = [
          {
            ...cronquistPlante,
          },
          { id: 456 },
        ];
        expectedResult = service.addCronquistPlanteToCollectionIfMissing(cronquistPlanteCollection, cronquistPlante);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CronquistPlante to an array that doesn't contain it", () => {
        const cronquistPlante: ICronquistPlante = { id: 123 };
        const cronquistPlanteCollection: ICronquistPlante[] = [{ id: 456 }];
        expectedResult = service.addCronquistPlanteToCollectionIfMissing(cronquistPlanteCollection, cronquistPlante);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cronquistPlante);
      });

      it('should add only unique CronquistPlante to an array', () => {
        const cronquistPlanteArray: ICronquistPlante[] = [{ id: 123 }, { id: 456 }, { id: 73299 }];
        const cronquistPlanteCollection: ICronquistPlante[] = [{ id: 123 }];
        expectedResult = service.addCronquistPlanteToCollectionIfMissing(cronquistPlanteCollection, ...cronquistPlanteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cronquistPlante: ICronquistPlante = { id: 123 };
        const cronquistPlante2: ICronquistPlante = { id: 456 };
        expectedResult = service.addCronquistPlanteToCollectionIfMissing([], cronquistPlante, cronquistPlante2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cronquistPlante);
        expect(expectedResult).toContain(cronquistPlante2);
      });

      it('should accept null and undefined values', () => {
        const cronquistPlante: ICronquistPlante = { id: 123 };
        expectedResult = service.addCronquistPlanteToCollectionIfMissing([], null, cronquistPlante, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cronquistPlante);
      });

      it('should return initial array if no CronquistPlante is added', () => {
        const cronquistPlanteCollection: ICronquistPlante[] = [{ id: 123 }];
        expectedResult = service.addCronquistPlanteToCollectionIfMissing(cronquistPlanteCollection, undefined, null);
        expect(expectedResult).toEqual(cronquistPlanteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
