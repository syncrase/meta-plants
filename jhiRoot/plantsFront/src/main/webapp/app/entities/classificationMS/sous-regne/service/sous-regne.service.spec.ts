import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISousRegne, SousRegne } from '../sous-regne.model';

import { SousRegneService } from './sous-regne.service';

describe('SousRegne Service', () => {
  let service: SousRegneService;
  let httpMock: HttpTestingController;
  let elemDefault: ISousRegne;
  let expectedResult: ISousRegne | ISousRegne[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SousRegneService);
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

    it('should create a SousRegne', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SousRegne()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SousRegne', () => {
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

    it('should partial update a SousRegne', () => {
      const patchObject = Object.assign(
        {
          nomFr: 'BBBBBB',
        },
        new SousRegne()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SousRegne', () => {
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

    it('should delete a SousRegne', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSousRegneToCollectionIfMissing', () => {
      it('should add a SousRegne to an empty array', () => {
        const sousRegne: ISousRegne = { id: 123 };
        expectedResult = service.addSousRegneToCollectionIfMissing([], sousRegne);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousRegne);
      });

      it('should not add a SousRegne to an array that contains it', () => {
        const sousRegne: ISousRegne = { id: 123 };
        const sousRegneCollection: ISousRegne[] = [
          {
            ...sousRegne,
          },
          { id: 456 },
        ];
        expectedResult = service.addSousRegneToCollectionIfMissing(sousRegneCollection, sousRegne);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SousRegne to an array that doesn't contain it", () => {
        const sousRegne: ISousRegne = { id: 123 };
        const sousRegneCollection: ISousRegne[] = [{ id: 456 }];
        expectedResult = service.addSousRegneToCollectionIfMissing(sousRegneCollection, sousRegne);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousRegne);
      });

      it('should add only unique SousRegne to an array', () => {
        const sousRegneArray: ISousRegne[] = [{ id: 123 }, { id: 456 }, { id: 82989 }];
        const sousRegneCollection: ISousRegne[] = [{ id: 123 }];
        expectedResult = service.addSousRegneToCollectionIfMissing(sousRegneCollection, ...sousRegneArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sousRegne: ISousRegne = { id: 123 };
        const sousRegne2: ISousRegne = { id: 456 };
        expectedResult = service.addSousRegneToCollectionIfMissing([], sousRegne, sousRegne2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousRegne);
        expect(expectedResult).toContain(sousRegne2);
      });

      it('should accept null and undefined values', () => {
        const sousRegne: ISousRegne = { id: 123 };
        expectedResult = service.addSousRegneToCollectionIfMissing([], null, sousRegne, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousRegne);
      });

      it('should return initial array if no SousRegne is added', () => {
        const sousRegneCollection: ISousRegne[] = [{ id: 123 }];
        expectedResult = service.addSousRegneToCollectionIfMissing(sousRegneCollection, undefined, null);
        expect(expectedResult).toEqual(sousRegneCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
