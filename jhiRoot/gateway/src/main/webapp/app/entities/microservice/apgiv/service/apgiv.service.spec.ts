import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAPGIV, APGIV } from '../apgiv.model';

import { APGIVService } from './apgiv.service';

describe('APGIV Service', () => {
  let service: APGIVService;
  let httpMock: HttpTestingController;
  let elemDefault: IAPGIV;
  let expectedResult: IAPGIV | IAPGIV[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(APGIVService);
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

    it('should create a APGIV', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new APGIV()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a APGIV', () => {
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

    it('should partial update a APGIV', () => {
      const patchObject = Object.assign(
        {
          ordre: 'BBBBBB',
          famille: 'BBBBBB',
        },
        new APGIV()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of APGIV', () => {
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

    it('should delete a APGIV', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAPGIVToCollectionIfMissing', () => {
      it('should add a APGIV to an empty array', () => {
        const aPGIV: IAPGIV = { id: 123 };
        expectedResult = service.addAPGIVToCollectionIfMissing([], aPGIV);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aPGIV);
      });

      it('should not add a APGIV to an array that contains it', () => {
        const aPGIV: IAPGIV = { id: 123 };
        const aPGIVCollection: IAPGIV[] = [
          {
            ...aPGIV,
          },
          { id: 456 },
        ];
        expectedResult = service.addAPGIVToCollectionIfMissing(aPGIVCollection, aPGIV);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a APGIV to an array that doesn't contain it", () => {
        const aPGIV: IAPGIV = { id: 123 };
        const aPGIVCollection: IAPGIV[] = [{ id: 456 }];
        expectedResult = service.addAPGIVToCollectionIfMissing(aPGIVCollection, aPGIV);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aPGIV);
      });

      it('should add only unique APGIV to an array', () => {
        const aPGIVArray: IAPGIV[] = [{ id: 123 }, { id: 456 }, { id: 74563 }];
        const aPGIVCollection: IAPGIV[] = [{ id: 123 }];
        expectedResult = service.addAPGIVToCollectionIfMissing(aPGIVCollection, ...aPGIVArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aPGIV: IAPGIV = { id: 123 };
        const aPGIV2: IAPGIV = { id: 456 };
        expectedResult = service.addAPGIVToCollectionIfMissing([], aPGIV, aPGIV2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aPGIV);
        expect(expectedResult).toContain(aPGIV2);
      });

      it('should accept null and undefined values', () => {
        const aPGIV: IAPGIV = { id: 123 };
        expectedResult = service.addAPGIVToCollectionIfMissing([], null, aPGIV, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aPGIV);
      });

      it('should return initial array if no APGIV is added', () => {
        const aPGIVCollection: IAPGIV[] = [{ id: 123 }];
        expectedResult = service.addAPGIVToCollectionIfMissing(aPGIVCollection, undefined, null);
        expect(expectedResult).toEqual(aPGIVCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
