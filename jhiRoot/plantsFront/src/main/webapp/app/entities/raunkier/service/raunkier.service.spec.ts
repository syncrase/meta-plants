import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRaunkier, Raunkier } from '../raunkier.model';

import { RaunkierService } from './raunkier.service';

describe('Raunkier Service', () => {
  let service: RaunkierService;
  let httpMock: HttpTestingController;
  let elemDefault: IRaunkier;
  let expectedResult: IRaunkier | IRaunkier[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RaunkierService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      type: 'AAAAAAA',
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

    it('should create a Raunkier', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Raunkier()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Raunkier', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          type: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Raunkier', () => {
      const patchObject = Object.assign(
        {
          type: 'BBBBBB',
        },
        new Raunkier()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Raunkier', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          type: 'BBBBBB',
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

    it('should delete a Raunkier', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRaunkierToCollectionIfMissing', () => {
      it('should add a Raunkier to an empty array', () => {
        const raunkier: IRaunkier = { id: 123 };
        expectedResult = service.addRaunkierToCollectionIfMissing([], raunkier);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(raunkier);
      });

      it('should not add a Raunkier to an array that contains it', () => {
        const raunkier: IRaunkier = { id: 123 };
        const raunkierCollection: IRaunkier[] = [
          {
            ...raunkier,
          },
          { id: 456 },
        ];
        expectedResult = service.addRaunkierToCollectionIfMissing(raunkierCollection, raunkier);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Raunkier to an array that doesn't contain it", () => {
        const raunkier: IRaunkier = { id: 123 };
        const raunkierCollection: IRaunkier[] = [{ id: 456 }];
        expectedResult = service.addRaunkierToCollectionIfMissing(raunkierCollection, raunkier);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(raunkier);
      });

      it('should add only unique Raunkier to an array', () => {
        const raunkierArray: IRaunkier[] = [{ id: 123 }, { id: 456 }, { id: 85242 }];
        const raunkierCollection: IRaunkier[] = [{ id: 123 }];
        expectedResult = service.addRaunkierToCollectionIfMissing(raunkierCollection, ...raunkierArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const raunkier: IRaunkier = { id: 123 };
        const raunkier2: IRaunkier = { id: 456 };
        expectedResult = service.addRaunkierToCollectionIfMissing([], raunkier, raunkier2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(raunkier);
        expect(expectedResult).toContain(raunkier2);
      });

      it('should accept null and undefined values', () => {
        const raunkier: IRaunkier = { id: 123 };
        expectedResult = service.addRaunkierToCollectionIfMissing([], null, raunkier, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(raunkier);
      });

      it('should return initial array if no Raunkier is added', () => {
        const raunkierCollection: IRaunkier[] = [{ id: 123 }];
        expectedResult = service.addRaunkierToCollectionIfMissing(raunkierCollection, undefined, null);
        expect(expectedResult).toEqual(raunkierCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
