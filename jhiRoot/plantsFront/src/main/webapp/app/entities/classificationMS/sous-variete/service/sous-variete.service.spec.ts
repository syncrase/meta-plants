import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISousVariete, SousVariete } from '../sous-variete.model';

import { SousVarieteService } from './sous-variete.service';

describe('SousVariete Service', () => {
  let service: SousVarieteService;
  let httpMock: HttpTestingController;
  let elemDefault: ISousVariete;
  let expectedResult: ISousVariete | ISousVariete[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SousVarieteService);
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

    it('should create a SousVariete', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SousVariete()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SousVariete', () => {
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

    it('should partial update a SousVariete', () => {
      const patchObject = Object.assign(
        {
          nomFr: 'BBBBBB',
        },
        new SousVariete()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SousVariete', () => {
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

    it('should delete a SousVariete', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSousVarieteToCollectionIfMissing', () => {
      it('should add a SousVariete to an empty array', () => {
        const sousVariete: ISousVariete = { id: 123 };
        expectedResult = service.addSousVarieteToCollectionIfMissing([], sousVariete);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousVariete);
      });

      it('should not add a SousVariete to an array that contains it', () => {
        const sousVariete: ISousVariete = { id: 123 };
        const sousVarieteCollection: ISousVariete[] = [
          {
            ...sousVariete,
          },
          { id: 456 },
        ];
        expectedResult = service.addSousVarieteToCollectionIfMissing(sousVarieteCollection, sousVariete);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SousVariete to an array that doesn't contain it", () => {
        const sousVariete: ISousVariete = { id: 123 };
        const sousVarieteCollection: ISousVariete[] = [{ id: 456 }];
        expectedResult = service.addSousVarieteToCollectionIfMissing(sousVarieteCollection, sousVariete);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousVariete);
      });

      it('should add only unique SousVariete to an array', () => {
        const sousVarieteArray: ISousVariete[] = [{ id: 123 }, { id: 456 }, { id: 85213 }];
        const sousVarieteCollection: ISousVariete[] = [{ id: 123 }];
        expectedResult = service.addSousVarieteToCollectionIfMissing(sousVarieteCollection, ...sousVarieteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sousVariete: ISousVariete = { id: 123 };
        const sousVariete2: ISousVariete = { id: 456 };
        expectedResult = service.addSousVarieteToCollectionIfMissing([], sousVariete, sousVariete2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousVariete);
        expect(expectedResult).toContain(sousVariete2);
      });

      it('should accept null and undefined values', () => {
        const sousVariete: ISousVariete = { id: 123 };
        expectedResult = service.addSousVarieteToCollectionIfMissing([], null, sousVariete, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousVariete);
      });

      it('should return initial array if no SousVariete is added', () => {
        const sousVarieteCollection: ISousVariete[] = [{ id: 123 }];
        expectedResult = service.addSousVarieteToCollectionIfMissing(sousVarieteCollection, undefined, null);
        expect(expectedResult).toEqual(sousVarieteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
