import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IClassificationNom, ClassificationNom } from '../classification-nom.model';

import { ClassificationNomService } from './classification-nom.service';

describe('ClassificationNom Service', () => {
  let service: ClassificationNomService;
  let httpMock: HttpTestingController;
  let elemDefault: IClassificationNom;
  let expectedResult: IClassificationNom | IClassificationNom[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ClassificationNomService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomFr: 'AAAAAAA',
      nomLatin: 'AAAAAAA',
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

    it('should create a ClassificationNom', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ClassificationNom()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ClassificationNom', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomFr: 'BBBBBB',
          nomLatin: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ClassificationNom', () => {
      const patchObject = Object.assign({}, new ClassificationNom());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ClassificationNom', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomFr: 'BBBBBB',
          nomLatin: 'BBBBBB',
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

    it('should delete a ClassificationNom', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addClassificationNomToCollectionIfMissing', () => {
      it('should add a ClassificationNom to an empty array', () => {
        const classificationNom: IClassificationNom = { id: 123 };
        expectedResult = service.addClassificationNomToCollectionIfMissing([], classificationNom);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(classificationNom);
      });

      it('should not add a ClassificationNom to an array that contains it', () => {
        const classificationNom: IClassificationNom = { id: 123 };
        const classificationNomCollection: IClassificationNom[] = [
          {
            ...classificationNom,
          },
          { id: 456 },
        ];
        expectedResult = service.addClassificationNomToCollectionIfMissing(classificationNomCollection, classificationNom);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ClassificationNom to an array that doesn't contain it", () => {
        const classificationNom: IClassificationNom = { id: 123 };
        const classificationNomCollection: IClassificationNom[] = [{ id: 456 }];
        expectedResult = service.addClassificationNomToCollectionIfMissing(classificationNomCollection, classificationNom);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(classificationNom);
      });

      it('should add only unique ClassificationNom to an array', () => {
        const classificationNomArray: IClassificationNom[] = [{ id: 123 }, { id: 456 }, { id: 25339 }];
        const classificationNomCollection: IClassificationNom[] = [{ id: 123 }];
        expectedResult = service.addClassificationNomToCollectionIfMissing(classificationNomCollection, ...classificationNomArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const classificationNom: IClassificationNom = { id: 123 };
        const classificationNom2: IClassificationNom = { id: 456 };
        expectedResult = service.addClassificationNomToCollectionIfMissing([], classificationNom, classificationNom2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(classificationNom);
        expect(expectedResult).toContain(classificationNom2);
      });

      it('should accept null and undefined values', () => {
        const classificationNom: IClassificationNom = { id: 123 };
        expectedResult = service.addClassificationNomToCollectionIfMissing([], null, classificationNom, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(classificationNom);
      });

      it('should return initial array if no ClassificationNom is added', () => {
        const classificationNomCollection: IClassificationNom[] = [{ id: 123 }];
        expectedResult = service.addClassificationNomToCollectionIfMissing(classificationNomCollection, undefined, null);
        expect(expectedResult).toEqual(classificationNomCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
