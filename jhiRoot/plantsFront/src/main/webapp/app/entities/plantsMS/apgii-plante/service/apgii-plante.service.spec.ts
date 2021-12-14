import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAPGIIPlante, APGIIPlante } from '../apgii-plante.model';

import { APGIIPlanteService } from './apgii-plante.service';

describe('APGIIPlante Service', () => {
  let service: APGIIPlanteService;
  let httpMock: HttpTestingController;
  let elemDefault: IAPGIIPlante;
  let expectedResult: IAPGIIPlante | IAPGIIPlante[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(APGIIPlanteService);
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

    it('should create a APGIIPlante', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new APGIIPlante()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a APGIIPlante', () => {
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

    it('should partial update a APGIIPlante', () => {
      const patchObject = Object.assign({}, new APGIIPlante());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of APGIIPlante', () => {
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

    it('should delete a APGIIPlante', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAPGIIPlanteToCollectionIfMissing', () => {
      it('should add a APGIIPlante to an empty array', () => {
        const aPGIIPlante: IAPGIIPlante = { id: 123 };
        expectedResult = service.addAPGIIPlanteToCollectionIfMissing([], aPGIIPlante);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aPGIIPlante);
      });

      it('should not add a APGIIPlante to an array that contains it', () => {
        const aPGIIPlante: IAPGIIPlante = { id: 123 };
        const aPGIIPlanteCollection: IAPGIIPlante[] = [
          {
            ...aPGIIPlante,
          },
          { id: 456 },
        ];
        expectedResult = service.addAPGIIPlanteToCollectionIfMissing(aPGIIPlanteCollection, aPGIIPlante);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a APGIIPlante to an array that doesn't contain it", () => {
        const aPGIIPlante: IAPGIIPlante = { id: 123 };
        const aPGIIPlanteCollection: IAPGIIPlante[] = [{ id: 456 }];
        expectedResult = service.addAPGIIPlanteToCollectionIfMissing(aPGIIPlanteCollection, aPGIIPlante);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aPGIIPlante);
      });

      it('should add only unique APGIIPlante to an array', () => {
        const aPGIIPlanteArray: IAPGIIPlante[] = [{ id: 123 }, { id: 456 }, { id: 59752 }];
        const aPGIIPlanteCollection: IAPGIIPlante[] = [{ id: 123 }];
        expectedResult = service.addAPGIIPlanteToCollectionIfMissing(aPGIIPlanteCollection, ...aPGIIPlanteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aPGIIPlante: IAPGIIPlante = { id: 123 };
        const aPGIIPlante2: IAPGIIPlante = { id: 456 };
        expectedResult = service.addAPGIIPlanteToCollectionIfMissing([], aPGIIPlante, aPGIIPlante2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aPGIIPlante);
        expect(expectedResult).toContain(aPGIIPlante2);
      });

      it('should accept null and undefined values', () => {
        const aPGIIPlante: IAPGIIPlante = { id: 123 };
        expectedResult = service.addAPGIIPlanteToCollectionIfMissing([], null, aPGIIPlante, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aPGIIPlante);
      });

      it('should return initial array if no APGIIPlante is added', () => {
        const aPGIIPlanteCollection: IAPGIIPlante[] = [{ id: 123 }];
        expectedResult = service.addAPGIIPlanteToCollectionIfMissing(aPGIIPlanteCollection, undefined, null);
        expect(expectedResult).toEqual(aPGIIPlanteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
