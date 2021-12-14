import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAPGIPlante, APGIPlante } from '../apgi-plante.model';

import { APGIPlanteService } from './apgi-plante.service';

describe('APGIPlante Service', () => {
  let service: APGIPlanteService;
  let httpMock: HttpTestingController;
  let elemDefault: IAPGIPlante;
  let expectedResult: IAPGIPlante | IAPGIPlante[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(APGIPlanteService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      ordre: 'AAAAAAA',
      famille: 'AAAAAAA',
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

    it('should create a APGIPlante', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new APGIPlante()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a APGIPlante', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          ordre: 'BBBBBB',
          famille: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a APGIPlante', () => {
      const patchObject = Object.assign(
        {
          ordre: 'BBBBBB',
        },
        new APGIPlante()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of APGIPlante', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          ordre: 'BBBBBB',
          famille: 'BBBBBB',
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

    it('should delete a APGIPlante', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAPGIPlanteToCollectionIfMissing', () => {
      it('should add a APGIPlante to an empty array', () => {
        const aPGIPlante: IAPGIPlante = { id: 123 };
        expectedResult = service.addAPGIPlanteToCollectionIfMissing([], aPGIPlante);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aPGIPlante);
      });

      it('should not add a APGIPlante to an array that contains it', () => {
        const aPGIPlante: IAPGIPlante = { id: 123 };
        const aPGIPlanteCollection: IAPGIPlante[] = [
          {
            ...aPGIPlante,
          },
          { id: 456 },
        ];
        expectedResult = service.addAPGIPlanteToCollectionIfMissing(aPGIPlanteCollection, aPGIPlante);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a APGIPlante to an array that doesn't contain it", () => {
        const aPGIPlante: IAPGIPlante = { id: 123 };
        const aPGIPlanteCollection: IAPGIPlante[] = [{ id: 456 }];
        expectedResult = service.addAPGIPlanteToCollectionIfMissing(aPGIPlanteCollection, aPGIPlante);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aPGIPlante);
      });

      it('should add only unique APGIPlante to an array', () => {
        const aPGIPlanteArray: IAPGIPlante[] = [{ id: 123 }, { id: 456 }, { id: 1889 }];
        const aPGIPlanteCollection: IAPGIPlante[] = [{ id: 123 }];
        expectedResult = service.addAPGIPlanteToCollectionIfMissing(aPGIPlanteCollection, ...aPGIPlanteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aPGIPlante: IAPGIPlante = { id: 123 };
        const aPGIPlante2: IAPGIPlante = { id: 456 };
        expectedResult = service.addAPGIPlanteToCollectionIfMissing([], aPGIPlante, aPGIPlante2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aPGIPlante);
        expect(expectedResult).toContain(aPGIPlante2);
      });

      it('should accept null and undefined values', () => {
        const aPGIPlante: IAPGIPlante = { id: 123 };
        expectedResult = service.addAPGIPlanteToCollectionIfMissing([], null, aPGIPlante, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aPGIPlante);
      });

      it('should return initial array if no APGIPlante is added', () => {
        const aPGIPlanteCollection: IAPGIPlante[] = [{ id: 123 }];
        expectedResult = service.addAPGIPlanteToCollectionIfMissing(aPGIPlanteCollection, undefined, null);
        expect(expectedResult).toEqual(aPGIPlanteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
