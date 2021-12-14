import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISousDivision, SousDivision } from '../sous-division.model';

import { SousDivisionService } from './sous-division.service';

describe('SousDivision Service', () => {
  let service: SousDivisionService;
  let httpMock: HttpTestingController;
  let elemDefault: ISousDivision;
  let expectedResult: ISousDivision | ISousDivision[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SousDivisionService);
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

    it('should create a SousDivision', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SousDivision()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SousDivision', () => {
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

    it('should partial update a SousDivision', () => {
      const patchObject = Object.assign(
        {
          nomFr: 'BBBBBB',
          nomLatin: 'BBBBBB',
        },
        new SousDivision()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SousDivision', () => {
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

    it('should delete a SousDivision', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSousDivisionToCollectionIfMissing', () => {
      it('should add a SousDivision to an empty array', () => {
        const sousDivision: ISousDivision = { id: 123 };
        expectedResult = service.addSousDivisionToCollectionIfMissing([], sousDivision);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousDivision);
      });

      it('should not add a SousDivision to an array that contains it', () => {
        const sousDivision: ISousDivision = { id: 123 };
        const sousDivisionCollection: ISousDivision[] = [
          {
            ...sousDivision,
          },
          { id: 456 },
        ];
        expectedResult = service.addSousDivisionToCollectionIfMissing(sousDivisionCollection, sousDivision);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SousDivision to an array that doesn't contain it", () => {
        const sousDivision: ISousDivision = { id: 123 };
        const sousDivisionCollection: ISousDivision[] = [{ id: 456 }];
        expectedResult = service.addSousDivisionToCollectionIfMissing(sousDivisionCollection, sousDivision);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousDivision);
      });

      it('should add only unique SousDivision to an array', () => {
        const sousDivisionArray: ISousDivision[] = [{ id: 123 }, { id: 456 }, { id: 73562 }];
        const sousDivisionCollection: ISousDivision[] = [{ id: 123 }];
        expectedResult = service.addSousDivisionToCollectionIfMissing(sousDivisionCollection, ...sousDivisionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sousDivision: ISousDivision = { id: 123 };
        const sousDivision2: ISousDivision = { id: 456 };
        expectedResult = service.addSousDivisionToCollectionIfMissing([], sousDivision, sousDivision2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousDivision);
        expect(expectedResult).toContain(sousDivision2);
      });

      it('should accept null and undefined values', () => {
        const sousDivision: ISousDivision = { id: 123 };
        expectedResult = service.addSousDivisionToCollectionIfMissing([], null, sousDivision, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousDivision);
      });

      it('should return initial array if no SousDivision is added', () => {
        const sousDivisionCollection: ISousDivision[] = [{ id: 123 }];
        expectedResult = service.addSousDivisionToCollectionIfMissing(sousDivisionCollection, undefined, null);
        expect(expectedResult).toEqual(sousDivisionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
