import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAPGII, APGII } from '../apgii.model';

import { APGIIService } from './apgii.service';

describe('APGII Service', () => {
  let service: APGIIService;
  let httpMock: HttpTestingController;
  let elemDefault: IAPGII;
  let expectedResult: IAPGII | IAPGII[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(APGIIService);
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

    it('should create a APGII', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new APGII()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a APGII', () => {
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

    it('should partial update a APGII', () => {
      const patchObject = Object.assign(
        {
          famille: 'BBBBBB',
        },
        new APGII()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of APGII', () => {
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

    it('should delete a APGII', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAPGIIToCollectionIfMissing', () => {
      it('should add a APGII to an empty array', () => {
        const aPGII: IAPGII = { id: 123 };
        expectedResult = service.addAPGIIToCollectionIfMissing([], aPGII);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aPGII);
      });

      it('should not add a APGII to an array that contains it', () => {
        const aPGII: IAPGII = { id: 123 };
        const aPGIICollection: IAPGII[] = [
          {
            ...aPGII,
          },
          { id: 456 },
        ];
        expectedResult = service.addAPGIIToCollectionIfMissing(aPGIICollection, aPGII);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a APGII to an array that doesn't contain it", () => {
        const aPGII: IAPGII = { id: 123 };
        const aPGIICollection: IAPGII[] = [{ id: 456 }];
        expectedResult = service.addAPGIIToCollectionIfMissing(aPGIICollection, aPGII);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aPGII);
      });

      it('should add only unique APGII to an array', () => {
        const aPGIIArray: IAPGII[] = [{ id: 123 }, { id: 456 }, { id: 71828 }];
        const aPGIICollection: IAPGII[] = [{ id: 123 }];
        expectedResult = service.addAPGIIToCollectionIfMissing(aPGIICollection, ...aPGIIArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aPGII: IAPGII = { id: 123 };
        const aPGII2: IAPGII = { id: 456 };
        expectedResult = service.addAPGIIToCollectionIfMissing([], aPGII, aPGII2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aPGII);
        expect(expectedResult).toContain(aPGII2);
      });

      it('should accept null and undefined values', () => {
        const aPGII: IAPGII = { id: 123 };
        expectedResult = service.addAPGIIToCollectionIfMissing([], null, aPGII, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aPGII);
      });

      it('should return initial array if no APGII is added', () => {
        const aPGIICollection: IAPGII[] = [{ id: 123 }];
        expectedResult = service.addAPGIIToCollectionIfMissing(aPGIICollection, undefined, null);
        expect(expectedResult).toEqual(aPGIICollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
