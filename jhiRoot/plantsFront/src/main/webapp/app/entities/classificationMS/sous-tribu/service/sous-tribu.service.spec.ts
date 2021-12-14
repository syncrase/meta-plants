import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISousTribu, SousTribu } from '../sous-tribu.model';

import { SousTribuService } from './sous-tribu.service';

describe('SousTribu Service', () => {
  let service: SousTribuService;
  let httpMock: HttpTestingController;
  let elemDefault: ISousTribu;
  let expectedResult: ISousTribu | ISousTribu[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SousTribuService);
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

    it('should create a SousTribu', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SousTribu()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SousTribu', () => {
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

    it('should partial update a SousTribu', () => {
      const patchObject = Object.assign({}, new SousTribu());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SousTribu', () => {
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

    it('should delete a SousTribu', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSousTribuToCollectionIfMissing', () => {
      it('should add a SousTribu to an empty array', () => {
        const sousTribu: ISousTribu = { id: 123 };
        expectedResult = service.addSousTribuToCollectionIfMissing([], sousTribu);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousTribu);
      });

      it('should not add a SousTribu to an array that contains it', () => {
        const sousTribu: ISousTribu = { id: 123 };
        const sousTribuCollection: ISousTribu[] = [
          {
            ...sousTribu,
          },
          { id: 456 },
        ];
        expectedResult = service.addSousTribuToCollectionIfMissing(sousTribuCollection, sousTribu);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SousTribu to an array that doesn't contain it", () => {
        const sousTribu: ISousTribu = { id: 123 };
        const sousTribuCollection: ISousTribu[] = [{ id: 456 }];
        expectedResult = service.addSousTribuToCollectionIfMissing(sousTribuCollection, sousTribu);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousTribu);
      });

      it('should add only unique SousTribu to an array', () => {
        const sousTribuArray: ISousTribu[] = [{ id: 123 }, { id: 456 }, { id: 74122 }];
        const sousTribuCollection: ISousTribu[] = [{ id: 123 }];
        expectedResult = service.addSousTribuToCollectionIfMissing(sousTribuCollection, ...sousTribuArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sousTribu: ISousTribu = { id: 123 };
        const sousTribu2: ISousTribu = { id: 456 };
        expectedResult = service.addSousTribuToCollectionIfMissing([], sousTribu, sousTribu2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousTribu);
        expect(expectedResult).toContain(sousTribu2);
      });

      it('should accept null and undefined values', () => {
        const sousTribu: ISousTribu = { id: 123 };
        expectedResult = service.addSousTribuToCollectionIfMissing([], null, sousTribu, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousTribu);
      });

      it('should return initial array if no SousTribu is added', () => {
        const sousTribuCollection: ISousTribu[] = [{ id: 123 }];
        expectedResult = service.addSousTribuToCollectionIfMissing(sousTribuCollection, undefined, null);
        expect(expectedResult).toEqual(sousTribuCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
