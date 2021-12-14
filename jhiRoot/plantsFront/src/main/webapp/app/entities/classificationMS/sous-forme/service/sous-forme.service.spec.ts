import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISousForme, SousForme } from '../sous-forme.model';

import { SousFormeService } from './sous-forme.service';

describe('SousForme Service', () => {
  let service: SousFormeService;
  let httpMock: HttpTestingController;
  let elemDefault: ISousForme;
  let expectedResult: ISousForme | ISousForme[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SousFormeService);
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

    it('should create a SousForme', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SousForme()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SousForme', () => {
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

    it('should partial update a SousForme', () => {
      const patchObject = Object.assign(
        {
          nomLatin: 'BBBBBB',
        },
        new SousForme()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SousForme', () => {
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

    it('should delete a SousForme', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSousFormeToCollectionIfMissing', () => {
      it('should add a SousForme to an empty array', () => {
        const sousForme: ISousForme = { id: 123 };
        expectedResult = service.addSousFormeToCollectionIfMissing([], sousForme);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousForme);
      });

      it('should not add a SousForme to an array that contains it', () => {
        const sousForme: ISousForme = { id: 123 };
        const sousFormeCollection: ISousForme[] = [
          {
            ...sousForme,
          },
          { id: 456 },
        ];
        expectedResult = service.addSousFormeToCollectionIfMissing(sousFormeCollection, sousForme);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SousForme to an array that doesn't contain it", () => {
        const sousForme: ISousForme = { id: 123 };
        const sousFormeCollection: ISousForme[] = [{ id: 456 }];
        expectedResult = service.addSousFormeToCollectionIfMissing(sousFormeCollection, sousForme);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousForme);
      });

      it('should add only unique SousForme to an array', () => {
        const sousFormeArray: ISousForme[] = [{ id: 123 }, { id: 456 }, { id: 13328 }];
        const sousFormeCollection: ISousForme[] = [{ id: 123 }];
        expectedResult = service.addSousFormeToCollectionIfMissing(sousFormeCollection, ...sousFormeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sousForme: ISousForme = { id: 123 };
        const sousForme2: ISousForme = { id: 456 };
        expectedResult = service.addSousFormeToCollectionIfMissing([], sousForme, sousForme2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousForme);
        expect(expectedResult).toContain(sousForme2);
      });

      it('should accept null and undefined values', () => {
        const sousForme: ISousForme = { id: 123 };
        expectedResult = service.addSousFormeToCollectionIfMissing([], null, sousForme, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousForme);
      });

      it('should return initial array if no SousForme is added', () => {
        const sousFormeCollection: ISousForme[] = [{ id: 123 }];
        expectedResult = service.addSousFormeToCollectionIfMissing(sousFormeCollection, undefined, null);
        expect(expectedResult).toEqual(sousFormeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
