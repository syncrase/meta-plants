import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IClassificationCronquist, ClassificationCronquist } from '../classification-cronquist.model';

import { ClassificationCronquistService } from './classification-cronquist.service';

describe('ClassificationCronquist Service', () => {
  let service: ClassificationCronquistService;
  let httpMock: HttpTestingController;
  let elemDefault: IClassificationCronquist;
  let expectedResult: IClassificationCronquist | IClassificationCronquist[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ClassificationCronquistService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      superRegne: 'AAAAAAA',
      regne: 'AAAAAAA',
      sousRegne: 'AAAAAAA',
      rameau: 'AAAAAAA',
      infraRegne: 'AAAAAAA',
      superEmbranchement: 'AAAAAAA',
      division: 'AAAAAAA',
      sousEmbranchement: 'AAAAAAA',
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

    it('should create a ClassificationCronquist', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ClassificationCronquist()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ClassificationCronquist', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          superRegne: 'BBBBBB',
          regne: 'BBBBBB',
          sousRegne: 'BBBBBB',
          rameau: 'BBBBBB',
          infraRegne: 'BBBBBB',
          superEmbranchement: 'BBBBBB',
          division: 'BBBBBB',
          sousEmbranchement: 'BBBBBB',
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

    it('should partial update a ClassificationCronquist', () => {
      const patchObject = Object.assign(
        {
          regne: 'BBBBBB',
          sousRegne: 'BBBBBB',
          rameau: 'BBBBBB',
          superEmbranchement: 'BBBBBB',
          sousEmbranchement: 'BBBBBB',
          microEmbranchement: 'BBBBBB',
          classe: 'BBBBBB',
          ordre: 'BBBBBB',
          sousOrdre: 'BBBBBB',
          microOrdre: 'BBBBBB',
          superFamille: 'BBBBBB',
          sousFamille: 'BBBBBB',
          sousTribu: 'BBBBBB',
          sousGenre: 'BBBBBB',
          section: 'BBBBBB',
          espece: 'BBBBBB',
          sousEspece: 'BBBBBB',
          sousVariete: 'BBBBBB',
        },
        new ClassificationCronquist()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ClassificationCronquist', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          superRegne: 'BBBBBB',
          regne: 'BBBBBB',
          sousRegne: 'BBBBBB',
          rameau: 'BBBBBB',
          infraRegne: 'BBBBBB',
          superEmbranchement: 'BBBBBB',
          division: 'BBBBBB',
          sousEmbranchement: 'BBBBBB',
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

    it('should delete a ClassificationCronquist', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addClassificationCronquistToCollectionIfMissing', () => {
      it('should add a ClassificationCronquist to an empty array', () => {
        const classificationCronquist: IClassificationCronquist = { id: 123 };
        expectedResult = service.addClassificationCronquistToCollectionIfMissing([], classificationCronquist);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(classificationCronquist);
      });

      it('should not add a ClassificationCronquist to an array that contains it', () => {
        const classificationCronquist: IClassificationCronquist = { id: 123 };
        const classificationCronquistCollection: IClassificationCronquist[] = [
          {
            ...classificationCronquist,
          },
          { id: 456 },
        ];
        expectedResult = service.addClassificationCronquistToCollectionIfMissing(
          classificationCronquistCollection,
          classificationCronquist
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ClassificationCronquist to an array that doesn't contain it", () => {
        const classificationCronquist: IClassificationCronquist = { id: 123 };
        const classificationCronquistCollection: IClassificationCronquist[] = [{ id: 456 }];
        expectedResult = service.addClassificationCronquistToCollectionIfMissing(
          classificationCronquistCollection,
          classificationCronquist
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(classificationCronquist);
      });

      it('should add only unique ClassificationCronquist to an array', () => {
        const classificationCronquistArray: IClassificationCronquist[] = [{ id: 123 }, { id: 456 }, { id: 63523 }];
        const classificationCronquistCollection: IClassificationCronquist[] = [{ id: 123 }];
        expectedResult = service.addClassificationCronquistToCollectionIfMissing(
          classificationCronquistCollection,
          ...classificationCronquistArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const classificationCronquist: IClassificationCronquist = { id: 123 };
        const classificationCronquist2: IClassificationCronquist = { id: 456 };
        expectedResult = service.addClassificationCronquistToCollectionIfMissing([], classificationCronquist, classificationCronquist2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(classificationCronquist);
        expect(expectedResult).toContain(classificationCronquist2);
      });

      it('should accept null and undefined values', () => {
        const classificationCronquist: IClassificationCronquist = { id: 123 };
        expectedResult = service.addClassificationCronquistToCollectionIfMissing([], null, classificationCronquist, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(classificationCronquist);
      });

      it('should return initial array if no ClassificationCronquist is added', () => {
        const classificationCronquistCollection: IClassificationCronquist[] = [{ id: 123 }];
        expectedResult = service.addClassificationCronquistToCollectionIfMissing(classificationCronquistCollection, undefined, null);
        expect(expectedResult).toEqual(classificationCronquistCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
