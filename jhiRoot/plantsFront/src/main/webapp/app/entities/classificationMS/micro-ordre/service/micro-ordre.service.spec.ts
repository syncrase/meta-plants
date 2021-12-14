import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMicroOrdre, MicroOrdre } from '../micro-ordre.model';

import { MicroOrdreService } from './micro-ordre.service';

describe('MicroOrdre Service', () => {
  let service: MicroOrdreService;
  let httpMock: HttpTestingController;
  let elemDefault: IMicroOrdre;
  let expectedResult: IMicroOrdre | IMicroOrdre[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MicroOrdreService);
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

    it('should create a MicroOrdre', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MicroOrdre()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MicroOrdre', () => {
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

    it('should partial update a MicroOrdre', () => {
      const patchObject = Object.assign(
        {
          nomFr: 'BBBBBB',
          nomLatin: 'BBBBBB',
        },
        new MicroOrdre()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MicroOrdre', () => {
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

    it('should delete a MicroOrdre', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMicroOrdreToCollectionIfMissing', () => {
      it('should add a MicroOrdre to an empty array', () => {
        const microOrdre: IMicroOrdre = { id: 123 };
        expectedResult = service.addMicroOrdreToCollectionIfMissing([], microOrdre);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(microOrdre);
      });

      it('should not add a MicroOrdre to an array that contains it', () => {
        const microOrdre: IMicroOrdre = { id: 123 };
        const microOrdreCollection: IMicroOrdre[] = [
          {
            ...microOrdre,
          },
          { id: 456 },
        ];
        expectedResult = service.addMicroOrdreToCollectionIfMissing(microOrdreCollection, microOrdre);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MicroOrdre to an array that doesn't contain it", () => {
        const microOrdre: IMicroOrdre = { id: 123 };
        const microOrdreCollection: IMicroOrdre[] = [{ id: 456 }];
        expectedResult = service.addMicroOrdreToCollectionIfMissing(microOrdreCollection, microOrdre);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(microOrdre);
      });

      it('should add only unique MicroOrdre to an array', () => {
        const microOrdreArray: IMicroOrdre[] = [{ id: 123 }, { id: 456 }, { id: 59171 }];
        const microOrdreCollection: IMicroOrdre[] = [{ id: 123 }];
        expectedResult = service.addMicroOrdreToCollectionIfMissing(microOrdreCollection, ...microOrdreArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const microOrdre: IMicroOrdre = { id: 123 };
        const microOrdre2: IMicroOrdre = { id: 456 };
        expectedResult = service.addMicroOrdreToCollectionIfMissing([], microOrdre, microOrdre2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(microOrdre);
        expect(expectedResult).toContain(microOrdre2);
      });

      it('should accept null and undefined values', () => {
        const microOrdre: IMicroOrdre = { id: 123 };
        expectedResult = service.addMicroOrdreToCollectionIfMissing([], null, microOrdre, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(microOrdre);
      });

      it('should return initial array if no MicroOrdre is added', () => {
        const microOrdreCollection: IMicroOrdre[] = [{ id: 123 }];
        expectedResult = service.addMicroOrdreToCollectionIfMissing(microOrdreCollection, undefined, null);
        expect(expectedResult).toEqual(microOrdreCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
