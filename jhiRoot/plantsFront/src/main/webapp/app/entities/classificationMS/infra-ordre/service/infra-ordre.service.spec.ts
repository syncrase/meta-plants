import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInfraOrdre, InfraOrdre } from '../infra-ordre.model';

import { InfraOrdreService } from './infra-ordre.service';

describe('InfraOrdre Service', () => {
  let service: InfraOrdreService;
  let httpMock: HttpTestingController;
  let elemDefault: IInfraOrdre;
  let expectedResult: IInfraOrdre | IInfraOrdre[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InfraOrdreService);
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

    it('should create a InfraOrdre', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new InfraOrdre()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InfraOrdre', () => {
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

    it('should partial update a InfraOrdre', () => {
      const patchObject = Object.assign(
        {
          nomFr: 'BBBBBB',
        },
        new InfraOrdre()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InfraOrdre', () => {
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

    it('should delete a InfraOrdre', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInfraOrdreToCollectionIfMissing', () => {
      it('should add a InfraOrdre to an empty array', () => {
        const infraOrdre: IInfraOrdre = { id: 123 };
        expectedResult = service.addInfraOrdreToCollectionIfMissing([], infraOrdre);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(infraOrdre);
      });

      it('should not add a InfraOrdre to an array that contains it', () => {
        const infraOrdre: IInfraOrdre = { id: 123 };
        const infraOrdreCollection: IInfraOrdre[] = [
          {
            ...infraOrdre,
          },
          { id: 456 },
        ];
        expectedResult = service.addInfraOrdreToCollectionIfMissing(infraOrdreCollection, infraOrdre);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InfraOrdre to an array that doesn't contain it", () => {
        const infraOrdre: IInfraOrdre = { id: 123 };
        const infraOrdreCollection: IInfraOrdre[] = [{ id: 456 }];
        expectedResult = service.addInfraOrdreToCollectionIfMissing(infraOrdreCollection, infraOrdre);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(infraOrdre);
      });

      it('should add only unique InfraOrdre to an array', () => {
        const infraOrdreArray: IInfraOrdre[] = [{ id: 123 }, { id: 456 }, { id: 91127 }];
        const infraOrdreCollection: IInfraOrdre[] = [{ id: 123 }];
        expectedResult = service.addInfraOrdreToCollectionIfMissing(infraOrdreCollection, ...infraOrdreArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const infraOrdre: IInfraOrdre = { id: 123 };
        const infraOrdre2: IInfraOrdre = { id: 456 };
        expectedResult = service.addInfraOrdreToCollectionIfMissing([], infraOrdre, infraOrdre2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(infraOrdre);
        expect(expectedResult).toContain(infraOrdre2);
      });

      it('should accept null and undefined values', () => {
        const infraOrdre: IInfraOrdre = { id: 123 };
        expectedResult = service.addInfraOrdreToCollectionIfMissing([], null, infraOrdre, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(infraOrdre);
      });

      it('should return initial array if no InfraOrdre is added', () => {
        const infraOrdreCollection: IInfraOrdre[] = [{ id: 123 }];
        expectedResult = service.addInfraOrdreToCollectionIfMissing(infraOrdreCollection, undefined, null);
        expect(expectedResult).toEqual(infraOrdreCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
