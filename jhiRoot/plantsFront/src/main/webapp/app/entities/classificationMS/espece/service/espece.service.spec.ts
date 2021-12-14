import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEspece, Espece } from '../espece.model';

import { EspeceService } from './espece.service';

describe('Espece Service', () => {
  let service: EspeceService;
  let httpMock: HttpTestingController;
  let elemDefault: IEspece;
  let expectedResult: IEspece | IEspece[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EspeceService);
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

    it('should create a Espece', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Espece()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Espece', () => {
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

    it('should partial update a Espece', () => {
      const patchObject = Object.assign({}, new Espece());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Espece', () => {
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

    it('should delete a Espece', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEspeceToCollectionIfMissing', () => {
      it('should add a Espece to an empty array', () => {
        const espece: IEspece = { id: 123 };
        expectedResult = service.addEspeceToCollectionIfMissing([], espece);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(espece);
      });

      it('should not add a Espece to an array that contains it', () => {
        const espece: IEspece = { id: 123 };
        const especeCollection: IEspece[] = [
          {
            ...espece,
          },
          { id: 456 },
        ];
        expectedResult = service.addEspeceToCollectionIfMissing(especeCollection, espece);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Espece to an array that doesn't contain it", () => {
        const espece: IEspece = { id: 123 };
        const especeCollection: IEspece[] = [{ id: 456 }];
        expectedResult = service.addEspeceToCollectionIfMissing(especeCollection, espece);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(espece);
      });

      it('should add only unique Espece to an array', () => {
        const especeArray: IEspece[] = [{ id: 123 }, { id: 456 }, { id: 90565 }];
        const especeCollection: IEspece[] = [{ id: 123 }];
        expectedResult = service.addEspeceToCollectionIfMissing(especeCollection, ...especeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const espece: IEspece = { id: 123 };
        const espece2: IEspece = { id: 456 };
        expectedResult = service.addEspeceToCollectionIfMissing([], espece, espece2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(espece);
        expect(expectedResult).toContain(espece2);
      });

      it('should accept null and undefined values', () => {
        const espece: IEspece = { id: 123 };
        expectedResult = service.addEspeceToCollectionIfMissing([], null, espece, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(espece);
      });

      it('should return initial array if no Espece is added', () => {
        const especeCollection: IEspece[] = [{ id: 123 }];
        expectedResult = service.addEspeceToCollectionIfMissing(especeCollection, undefined, null);
        expect(expectedResult).toEqual(especeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
