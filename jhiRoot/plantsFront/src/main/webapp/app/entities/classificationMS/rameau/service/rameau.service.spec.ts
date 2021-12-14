import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRameau, Rameau } from '../rameau.model';

import { RameauService } from './rameau.service';

describe('Rameau Service', () => {
  let service: RameauService;
  let httpMock: HttpTestingController;
  let elemDefault: IRameau;
  let expectedResult: IRameau | IRameau[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RameauService);
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

    it('should create a Rameau', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Rameau()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Rameau', () => {
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

    it('should partial update a Rameau', () => {
      const patchObject = Object.assign({}, new Rameau());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Rameau', () => {
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

    it('should delete a Rameau', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRameauToCollectionIfMissing', () => {
      it('should add a Rameau to an empty array', () => {
        const rameau: IRameau = { id: 123 };
        expectedResult = service.addRameauToCollectionIfMissing([], rameau);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rameau);
      });

      it('should not add a Rameau to an array that contains it', () => {
        const rameau: IRameau = { id: 123 };
        const rameauCollection: IRameau[] = [
          {
            ...rameau,
          },
          { id: 456 },
        ];
        expectedResult = service.addRameauToCollectionIfMissing(rameauCollection, rameau);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Rameau to an array that doesn't contain it", () => {
        const rameau: IRameau = { id: 123 };
        const rameauCollection: IRameau[] = [{ id: 456 }];
        expectedResult = service.addRameauToCollectionIfMissing(rameauCollection, rameau);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rameau);
      });

      it('should add only unique Rameau to an array', () => {
        const rameauArray: IRameau[] = [{ id: 123 }, { id: 456 }, { id: 78122 }];
        const rameauCollection: IRameau[] = [{ id: 123 }];
        expectedResult = service.addRameauToCollectionIfMissing(rameauCollection, ...rameauArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rameau: IRameau = { id: 123 };
        const rameau2: IRameau = { id: 456 };
        expectedResult = service.addRameauToCollectionIfMissing([], rameau, rameau2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rameau);
        expect(expectedResult).toContain(rameau2);
      });

      it('should accept null and undefined values', () => {
        const rameau: IRameau = { id: 123 };
        expectedResult = service.addRameauToCollectionIfMissing([], null, rameau, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rameau);
      });

      it('should return initial array if no Rameau is added', () => {
        const rameauCollection: IRameau[] = [{ id: 123 }];
        expectedResult = service.addRameauToCollectionIfMissing(rameauCollection, undefined, null);
        expect(expectedResult).toEqual(rameauCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
