import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAPGI, APGI } from '../apgi.model';

import { APGIService } from './apgi.service';

describe('APGI Service', () => {
  let service: APGIService;
  let httpMock: HttpTestingController;
  let elemDefault: IAPGI;
  let expectedResult: IAPGI | IAPGI[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(APGIService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      ordre: 'AAAAAAA',
      famille: 'AAAAAAA',
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

    it('should create a APGI', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new APGI()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a APGI', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          ordre: 'BBBBBB',
          famille: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a APGI', () => {
      const patchObject = Object.assign(
        {
          ordre: 'BBBBBB',
          famille: 'BBBBBB',
        },
        new APGI()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of APGI', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          ordre: 'BBBBBB',
          famille: 'BBBBBB',
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

    it('should delete a APGI', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAPGIToCollectionIfMissing', () => {
      it('should add a APGI to an empty array', () => {
        const aPGI: IAPGI = { id: 123 };
        expectedResult = service.addAPGIToCollectionIfMissing([], aPGI);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aPGI);
      });

      it('should not add a APGI to an array that contains it', () => {
        const aPGI: IAPGI = { id: 123 };
        const aPGICollection: IAPGI[] = [
          {
            ...aPGI,
          },
          { id: 456 },
        ];
        expectedResult = service.addAPGIToCollectionIfMissing(aPGICollection, aPGI);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a APGI to an array that doesn't contain it", () => {
        const aPGI: IAPGI = { id: 123 };
        const aPGICollection: IAPGI[] = [{ id: 456 }];
        expectedResult = service.addAPGIToCollectionIfMissing(aPGICollection, aPGI);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aPGI);
      });

      it('should add only unique APGI to an array', () => {
        const aPGIArray: IAPGI[] = [{ id: 123 }, { id: 456 }, { id: 33696 }];
        const aPGICollection: IAPGI[] = [{ id: 123 }];
        expectedResult = service.addAPGIToCollectionIfMissing(aPGICollection, ...aPGIArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aPGI: IAPGI = { id: 123 };
        const aPGI2: IAPGI = { id: 456 };
        expectedResult = service.addAPGIToCollectionIfMissing([], aPGI, aPGI2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aPGI);
        expect(expectedResult).toContain(aPGI2);
      });

      it('should accept null and undefined values', () => {
        const aPGI: IAPGI = { id: 123 };
        expectedResult = service.addAPGIToCollectionIfMissing([], null, aPGI, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aPGI);
      });

      it('should return initial array if no APGI is added', () => {
        const aPGICollection: IAPGI[] = [{ id: 123 }];
        expectedResult = service.addAPGIToCollectionIfMissing(aPGICollection, undefined, null);
        expect(expectedResult).toEqual(aPGICollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
