import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrdre, Ordre } from '../ordre.model';

import { OrdreService } from './ordre.service';

describe('Ordre Service', () => {
  let service: OrdreService;
  let httpMock: HttpTestingController;
  let elemDefault: IOrdre;
  let expectedResult: IOrdre | IOrdre[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrdreService);
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

    it('should create a Ordre', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Ordre()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ordre', () => {
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

    it('should partial update a Ordre', () => {
      const patchObject = Object.assign({}, new Ordre());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ordre', () => {
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

    it('should delete a Ordre', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOrdreToCollectionIfMissing', () => {
      it('should add a Ordre to an empty array', () => {
        const ordre: IOrdre = { id: 123 };
        expectedResult = service.addOrdreToCollectionIfMissing([], ordre);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ordre);
      });

      it('should not add a Ordre to an array that contains it', () => {
        const ordre: IOrdre = { id: 123 };
        const ordreCollection: IOrdre[] = [
          {
            ...ordre,
          },
          { id: 456 },
        ];
        expectedResult = service.addOrdreToCollectionIfMissing(ordreCollection, ordre);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ordre to an array that doesn't contain it", () => {
        const ordre: IOrdre = { id: 123 };
        const ordreCollection: IOrdre[] = [{ id: 456 }];
        expectedResult = service.addOrdreToCollectionIfMissing(ordreCollection, ordre);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ordre);
      });

      it('should add only unique Ordre to an array', () => {
        const ordreArray: IOrdre[] = [{ id: 123 }, { id: 456 }, { id: 5823 }];
        const ordreCollection: IOrdre[] = [{ id: 123 }];
        expectedResult = service.addOrdreToCollectionIfMissing(ordreCollection, ...ordreArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ordre: IOrdre = { id: 123 };
        const ordre2: IOrdre = { id: 456 };
        expectedResult = service.addOrdreToCollectionIfMissing([], ordre, ordre2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ordre);
        expect(expectedResult).toContain(ordre2);
      });

      it('should accept null and undefined values', () => {
        const ordre: IOrdre = { id: 123 };
        expectedResult = service.addOrdreToCollectionIfMissing([], null, ordre, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ordre);
      });

      it('should return initial array if no Ordre is added', () => {
        const ordreCollection: IOrdre[] = [{ id: 123 }];
        expectedResult = service.addOrdreToCollectionIfMissing(ordreCollection, undefined, null);
        expect(expectedResult).toEqual(ordreCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
