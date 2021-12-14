import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISousOrdre, SousOrdre } from '../sous-ordre.model';

import { SousOrdreService } from './sous-ordre.service';

describe('SousOrdre Service', () => {
  let service: SousOrdreService;
  let httpMock: HttpTestingController;
  let elemDefault: ISousOrdre;
  let expectedResult: ISousOrdre | ISousOrdre[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SousOrdreService);
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

    it('should create a SousOrdre', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SousOrdre()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SousOrdre', () => {
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

    it('should partial update a SousOrdre', () => {
      const patchObject = Object.assign({}, new SousOrdre());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SousOrdre', () => {
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

    it('should delete a SousOrdre', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSousOrdreToCollectionIfMissing', () => {
      it('should add a SousOrdre to an empty array', () => {
        const sousOrdre: ISousOrdre = { id: 123 };
        expectedResult = service.addSousOrdreToCollectionIfMissing([], sousOrdre);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousOrdre);
      });

      it('should not add a SousOrdre to an array that contains it', () => {
        const sousOrdre: ISousOrdre = { id: 123 };
        const sousOrdreCollection: ISousOrdre[] = [
          {
            ...sousOrdre,
          },
          { id: 456 },
        ];
        expectedResult = service.addSousOrdreToCollectionIfMissing(sousOrdreCollection, sousOrdre);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SousOrdre to an array that doesn't contain it", () => {
        const sousOrdre: ISousOrdre = { id: 123 };
        const sousOrdreCollection: ISousOrdre[] = [{ id: 456 }];
        expectedResult = service.addSousOrdreToCollectionIfMissing(sousOrdreCollection, sousOrdre);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousOrdre);
      });

      it('should add only unique SousOrdre to an array', () => {
        const sousOrdreArray: ISousOrdre[] = [{ id: 123 }, { id: 456 }, { id: 51678 }];
        const sousOrdreCollection: ISousOrdre[] = [{ id: 123 }];
        expectedResult = service.addSousOrdreToCollectionIfMissing(sousOrdreCollection, ...sousOrdreArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sousOrdre: ISousOrdre = { id: 123 };
        const sousOrdre2: ISousOrdre = { id: 456 };
        expectedResult = service.addSousOrdreToCollectionIfMissing([], sousOrdre, sousOrdre2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousOrdre);
        expect(expectedResult).toContain(sousOrdre2);
      });

      it('should accept null and undefined values', () => {
        const sousOrdre: ISousOrdre = { id: 123 };
        expectedResult = service.addSousOrdreToCollectionIfMissing([], null, sousOrdre, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousOrdre);
      });

      it('should return initial array if no SousOrdre is added', () => {
        const sousOrdreCollection: ISousOrdre[] = [{ id: 123 }];
        expectedResult = service.addSousOrdreToCollectionIfMissing(sousOrdreCollection, undefined, null);
        expect(expectedResult).toEqual(sousOrdreCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
