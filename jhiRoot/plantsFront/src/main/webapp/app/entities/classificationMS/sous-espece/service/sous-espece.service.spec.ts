import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISousEspece, SousEspece } from '../sous-espece.model';

import { SousEspeceService } from './sous-espece.service';

describe('SousEspece Service', () => {
  let service: SousEspeceService;
  let httpMock: HttpTestingController;
  let elemDefault: ISousEspece;
  let expectedResult: ISousEspece | ISousEspece[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SousEspeceService);
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

    it('should create a SousEspece', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SousEspece()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SousEspece', () => {
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

    it('should partial update a SousEspece', () => {
      const patchObject = Object.assign(
        {
          nomLatin: 'BBBBBB',
        },
        new SousEspece()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SousEspece', () => {
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

    it('should delete a SousEspece', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSousEspeceToCollectionIfMissing', () => {
      it('should add a SousEspece to an empty array', () => {
        const sousEspece: ISousEspece = { id: 123 };
        expectedResult = service.addSousEspeceToCollectionIfMissing([], sousEspece);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousEspece);
      });

      it('should not add a SousEspece to an array that contains it', () => {
        const sousEspece: ISousEspece = { id: 123 };
        const sousEspeceCollection: ISousEspece[] = [
          {
            ...sousEspece,
          },
          { id: 456 },
        ];
        expectedResult = service.addSousEspeceToCollectionIfMissing(sousEspeceCollection, sousEspece);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SousEspece to an array that doesn't contain it", () => {
        const sousEspece: ISousEspece = { id: 123 };
        const sousEspeceCollection: ISousEspece[] = [{ id: 456 }];
        expectedResult = service.addSousEspeceToCollectionIfMissing(sousEspeceCollection, sousEspece);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousEspece);
      });

      it('should add only unique SousEspece to an array', () => {
        const sousEspeceArray: ISousEspece[] = [{ id: 123 }, { id: 456 }, { id: 86830 }];
        const sousEspeceCollection: ISousEspece[] = [{ id: 123 }];
        expectedResult = service.addSousEspeceToCollectionIfMissing(sousEspeceCollection, ...sousEspeceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sousEspece: ISousEspece = { id: 123 };
        const sousEspece2: ISousEspece = { id: 456 };
        expectedResult = service.addSousEspeceToCollectionIfMissing([], sousEspece, sousEspece2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousEspece);
        expect(expectedResult).toContain(sousEspece2);
      });

      it('should accept null and undefined values', () => {
        const sousEspece: ISousEspece = { id: 123 };
        expectedResult = service.addSousEspeceToCollectionIfMissing([], null, sousEspece, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousEspece);
      });

      it('should return initial array if no SousEspece is added', () => {
        const sousEspeceCollection: ISousEspece[] = [{ id: 123 }];
        expectedResult = service.addSousEspeceToCollectionIfMissing(sousEspeceCollection, undefined, null);
        expect(expectedResult).toEqual(sousEspeceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
