import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISousGenre, SousGenre } from '../sous-genre.model';

import { SousGenreService } from './sous-genre.service';

describe('SousGenre Service', () => {
  let service: SousGenreService;
  let httpMock: HttpTestingController;
  let elemDefault: ISousGenre;
  let expectedResult: ISousGenre | ISousGenre[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SousGenreService);
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

    it('should create a SousGenre', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SousGenre()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SousGenre', () => {
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

    it('should partial update a SousGenre', () => {
      const patchObject = Object.assign(
        {
          nomFr: 'BBBBBB',
          nomLatin: 'BBBBBB',
        },
        new SousGenre()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SousGenre', () => {
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

    it('should delete a SousGenre', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSousGenreToCollectionIfMissing', () => {
      it('should add a SousGenre to an empty array', () => {
        const sousGenre: ISousGenre = { id: 123 };
        expectedResult = service.addSousGenreToCollectionIfMissing([], sousGenre);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousGenre);
      });

      it('should not add a SousGenre to an array that contains it', () => {
        const sousGenre: ISousGenre = { id: 123 };
        const sousGenreCollection: ISousGenre[] = [
          {
            ...sousGenre,
          },
          { id: 456 },
        ];
        expectedResult = service.addSousGenreToCollectionIfMissing(sousGenreCollection, sousGenre);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SousGenre to an array that doesn't contain it", () => {
        const sousGenre: ISousGenre = { id: 123 };
        const sousGenreCollection: ISousGenre[] = [{ id: 456 }];
        expectedResult = service.addSousGenreToCollectionIfMissing(sousGenreCollection, sousGenre);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousGenre);
      });

      it('should add only unique SousGenre to an array', () => {
        const sousGenreArray: ISousGenre[] = [{ id: 123 }, { id: 456 }, { id: 33789 }];
        const sousGenreCollection: ISousGenre[] = [{ id: 123 }];
        expectedResult = service.addSousGenreToCollectionIfMissing(sousGenreCollection, ...sousGenreArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sousGenre: ISousGenre = { id: 123 };
        const sousGenre2: ISousGenre = { id: 456 };
        expectedResult = service.addSousGenreToCollectionIfMissing([], sousGenre, sousGenre2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousGenre);
        expect(expectedResult).toContain(sousGenre2);
      });

      it('should accept null and undefined values', () => {
        const sousGenre: ISousGenre = { id: 123 };
        expectedResult = service.addSousGenreToCollectionIfMissing([], null, sousGenre, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousGenre);
      });

      it('should return initial array if no SousGenre is added', () => {
        const sousGenreCollection: ISousGenre[] = [{ id: 123 }];
        expectedResult = service.addSousGenreToCollectionIfMissing(sousGenreCollection, undefined, null);
        expect(expectedResult).toEqual(sousGenreCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
