import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IClade, Clade } from '../clade.model';

import { CladeService } from './clade.service';

describe('Clade Service', () => {
  let service: CladeService;
  let httpMock: HttpTestingController;
  let elemDefault: IClade;
  let expectedResult: IClade | IClade[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CladeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nom: 'AAAAAAA',
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

    it('should create a Clade', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Clade()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Clade', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Clade', () => {
      const patchObject = Object.assign(
        {
          nom: 'BBBBBB',
        },
        new Clade()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Clade', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nom: 'BBBBBB',
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

    it('should delete a Clade', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCladeToCollectionIfMissing', () => {
      it('should add a Clade to an empty array', () => {
        const clade: IClade = { id: 123 };
        expectedResult = service.addCladeToCollectionIfMissing([], clade);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(clade);
      });

      it('should not add a Clade to an array that contains it', () => {
        const clade: IClade = { id: 123 };
        const cladeCollection: IClade[] = [
          {
            ...clade,
          },
          { id: 456 },
        ];
        expectedResult = service.addCladeToCollectionIfMissing(cladeCollection, clade);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Clade to an array that doesn't contain it", () => {
        const clade: IClade = { id: 123 };
        const cladeCollection: IClade[] = [{ id: 456 }];
        expectedResult = service.addCladeToCollectionIfMissing(cladeCollection, clade);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(clade);
      });

      it('should add only unique Clade to an array', () => {
        const cladeArray: IClade[] = [{ id: 123 }, { id: 456 }, { id: 72546 }];
        const cladeCollection: IClade[] = [{ id: 123 }];
        expectedResult = service.addCladeToCollectionIfMissing(cladeCollection, ...cladeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const clade: IClade = { id: 123 };
        const clade2: IClade = { id: 456 };
        expectedResult = service.addCladeToCollectionIfMissing([], clade, clade2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(clade);
        expect(expectedResult).toContain(clade2);
      });

      it('should accept null and undefined values', () => {
        const clade: IClade = { id: 123 };
        expectedResult = service.addCladeToCollectionIfMissing([], null, clade, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(clade);
      });

      it('should return initial array if no Clade is added', () => {
        const cladeCollection: IClade[] = [{ id: 123 }];
        expectedResult = service.addCladeToCollectionIfMissing(cladeCollection, undefined, null);
        expect(expectedResult).toEqual(cladeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
