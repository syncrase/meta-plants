import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISuperDivision, SuperDivision } from '../super-division.model';

import { SuperDivisionService } from './super-division.service';

describe('SuperDivision Service', () => {
  let service: SuperDivisionService;
  let httpMock: HttpTestingController;
  let elemDefault: ISuperDivision;
  let expectedResult: ISuperDivision | ISuperDivision[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SuperDivisionService);
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

    it('should create a SuperDivision', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SuperDivision()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SuperDivision', () => {
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

    it('should partial update a SuperDivision', () => {
      const patchObject = Object.assign(
        {
          nomFr: 'BBBBBB',
          nomLatin: 'BBBBBB',
        },
        new SuperDivision()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SuperDivision', () => {
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

    it('should delete a SuperDivision', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSuperDivisionToCollectionIfMissing', () => {
      it('should add a SuperDivision to an empty array', () => {
        const superDivision: ISuperDivision = { id: 123 };
        expectedResult = service.addSuperDivisionToCollectionIfMissing([], superDivision);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(superDivision);
      });

      it('should not add a SuperDivision to an array that contains it', () => {
        const superDivision: ISuperDivision = { id: 123 };
        const superDivisionCollection: ISuperDivision[] = [
          {
            ...superDivision,
          },
          { id: 456 },
        ];
        expectedResult = service.addSuperDivisionToCollectionIfMissing(superDivisionCollection, superDivision);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SuperDivision to an array that doesn't contain it", () => {
        const superDivision: ISuperDivision = { id: 123 };
        const superDivisionCollection: ISuperDivision[] = [{ id: 456 }];
        expectedResult = service.addSuperDivisionToCollectionIfMissing(superDivisionCollection, superDivision);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(superDivision);
      });

      it('should add only unique SuperDivision to an array', () => {
        const superDivisionArray: ISuperDivision[] = [{ id: 123 }, { id: 456 }, { id: 9153 }];
        const superDivisionCollection: ISuperDivision[] = [{ id: 123 }];
        expectedResult = service.addSuperDivisionToCollectionIfMissing(superDivisionCollection, ...superDivisionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const superDivision: ISuperDivision = { id: 123 };
        const superDivision2: ISuperDivision = { id: 456 };
        expectedResult = service.addSuperDivisionToCollectionIfMissing([], superDivision, superDivision2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(superDivision);
        expect(expectedResult).toContain(superDivision2);
      });

      it('should accept null and undefined values', () => {
        const superDivision: ISuperDivision = { id: 123 };
        expectedResult = service.addSuperDivisionToCollectionIfMissing([], null, superDivision, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(superDivision);
      });

      it('should return initial array if no SuperDivision is added', () => {
        const superDivisionCollection: ISuperDivision[] = [{ id: 123 }];
        expectedResult = service.addSuperDivisionToCollectionIfMissing(superDivisionCollection, undefined, null);
        expect(expectedResult).toEqual(superDivisionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
