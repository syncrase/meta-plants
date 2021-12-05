import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAPGIII, APGIII } from '../apgiii.model';

import { APGIIIService } from './apgiii.service';

describe('APGIII Service', () => {
  let service: APGIIIService;
  let httpMock: HttpTestingController;
  let elemDefault: IAPGIII;
  let expectedResult: IAPGIII | IAPGIII[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(APGIIIService);
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

    it('should create a APGIII', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new APGIII()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a APGIII', () => {
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

    it('should partial update a APGIII', () => {
      const patchObject = Object.assign(
        {
          ordre: 'BBBBBB',
          famille: 'BBBBBB',
        },
        new APGIII()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of APGIII', () => {
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

    it('should delete a APGIII', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAPGIIIToCollectionIfMissing', () => {
      it('should add a APGIII to an empty array', () => {
        const aPGIII: IAPGIII = { id: 123 };
        expectedResult = service.addAPGIIIToCollectionIfMissing([], aPGIII);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aPGIII);
      });

      it('should not add a APGIII to an array that contains it', () => {
        const aPGIII: IAPGIII = { id: 123 };
        const aPGIIICollection: IAPGIII[] = [
          {
            ...aPGIII,
          },
          { id: 456 },
        ];
        expectedResult = service.addAPGIIIToCollectionIfMissing(aPGIIICollection, aPGIII);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a APGIII to an array that doesn't contain it", () => {
        const aPGIII: IAPGIII = { id: 123 };
        const aPGIIICollection: IAPGIII[] = [{ id: 456 }];
        expectedResult = service.addAPGIIIToCollectionIfMissing(aPGIIICollection, aPGIII);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aPGIII);
      });

      it('should add only unique APGIII to an array', () => {
        const aPGIIIArray: IAPGIII[] = [{ id: 123 }, { id: 456 }, { id: 45646 }];
        const aPGIIICollection: IAPGIII[] = [{ id: 123 }];
        expectedResult = service.addAPGIIIToCollectionIfMissing(aPGIIICollection, ...aPGIIIArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aPGIII: IAPGIII = { id: 123 };
        const aPGIII2: IAPGIII = { id: 456 };
        expectedResult = service.addAPGIIIToCollectionIfMissing([], aPGIII, aPGIII2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aPGIII);
        expect(expectedResult).toContain(aPGIII2);
      });

      it('should accept null and undefined values', () => {
        const aPGIII: IAPGIII = { id: 123 };
        expectedResult = service.addAPGIIIToCollectionIfMissing([], null, aPGIII, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aPGIII);
      });

      it('should return initial array if no APGIII is added', () => {
        const aPGIIICollection: IAPGIII[] = [{ id: 123 }];
        expectedResult = service.addAPGIIIToCollectionIfMissing(aPGIIICollection, undefined, null);
        expect(expectedResult).toEqual(aPGIIICollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
