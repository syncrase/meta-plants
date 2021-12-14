import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISuperOrdre, SuperOrdre } from '../super-ordre.model';

import { SuperOrdreService } from './super-ordre.service';

describe('SuperOrdre Service', () => {
  let service: SuperOrdreService;
  let httpMock: HttpTestingController;
  let elemDefault: ISuperOrdre;
  let expectedResult: ISuperOrdre | ISuperOrdre[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SuperOrdreService);
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

    it('should create a SuperOrdre', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SuperOrdre()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SuperOrdre', () => {
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

    it('should partial update a SuperOrdre', () => {
      const patchObject = Object.assign(
        {
          nomFr: 'BBBBBB',
          nomLatin: 'BBBBBB',
        },
        new SuperOrdre()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SuperOrdre', () => {
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

    it('should delete a SuperOrdre', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSuperOrdreToCollectionIfMissing', () => {
      it('should add a SuperOrdre to an empty array', () => {
        const superOrdre: ISuperOrdre = { id: 123 };
        expectedResult = service.addSuperOrdreToCollectionIfMissing([], superOrdre);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(superOrdre);
      });

      it('should not add a SuperOrdre to an array that contains it', () => {
        const superOrdre: ISuperOrdre = { id: 123 };
        const superOrdreCollection: ISuperOrdre[] = [
          {
            ...superOrdre,
          },
          { id: 456 },
        ];
        expectedResult = service.addSuperOrdreToCollectionIfMissing(superOrdreCollection, superOrdre);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SuperOrdre to an array that doesn't contain it", () => {
        const superOrdre: ISuperOrdre = { id: 123 };
        const superOrdreCollection: ISuperOrdre[] = [{ id: 456 }];
        expectedResult = service.addSuperOrdreToCollectionIfMissing(superOrdreCollection, superOrdre);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(superOrdre);
      });

      it('should add only unique SuperOrdre to an array', () => {
        const superOrdreArray: ISuperOrdre[] = [{ id: 123 }, { id: 456 }, { id: 28030 }];
        const superOrdreCollection: ISuperOrdre[] = [{ id: 123 }];
        expectedResult = service.addSuperOrdreToCollectionIfMissing(superOrdreCollection, ...superOrdreArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const superOrdre: ISuperOrdre = { id: 123 };
        const superOrdre2: ISuperOrdre = { id: 456 };
        expectedResult = service.addSuperOrdreToCollectionIfMissing([], superOrdre, superOrdre2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(superOrdre);
        expect(expectedResult).toContain(superOrdre2);
      });

      it('should accept null and undefined values', () => {
        const superOrdre: ISuperOrdre = { id: 123 };
        expectedResult = service.addSuperOrdreToCollectionIfMissing([], null, superOrdre, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(superOrdre);
      });

      it('should return initial array if no SuperOrdre is added', () => {
        const superOrdreCollection: ISuperOrdre[] = [{ id: 123 }];
        expectedResult = service.addSuperOrdreToCollectionIfMissing(superOrdreCollection, undefined, null);
        expect(expectedResult).toEqual(superOrdreCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
