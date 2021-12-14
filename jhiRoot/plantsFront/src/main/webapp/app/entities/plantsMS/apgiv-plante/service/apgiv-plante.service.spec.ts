import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAPGIVPlante, APGIVPlante } from '../apgiv-plante.model';

import { APGIVPlanteService } from './apgiv-plante.service';

describe('APGIVPlante Service', () => {
  let service: APGIVPlanteService;
  let httpMock: HttpTestingController;
  let elemDefault: IAPGIVPlante;
  let expectedResult: IAPGIVPlante | IAPGIVPlante[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(APGIVPlanteService);
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

    it('should create a APGIVPlante', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new APGIVPlante()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a APGIVPlante', () => {
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

    it('should partial update a APGIVPlante', () => {
      const patchObject = Object.assign(
        {
          ordre: 'BBBBBB',
          famille: 'BBBBBB',
        },
        new APGIVPlante()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of APGIVPlante', () => {
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

    it('should delete a APGIVPlante', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAPGIVPlanteToCollectionIfMissing', () => {
      it('should add a APGIVPlante to an empty array', () => {
        const aPGIVPlante: IAPGIVPlante = { id: 123 };
        expectedResult = service.addAPGIVPlanteToCollectionIfMissing([], aPGIVPlante);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aPGIVPlante);
      });

      it('should not add a APGIVPlante to an array that contains it', () => {
        const aPGIVPlante: IAPGIVPlante = { id: 123 };
        const aPGIVPlanteCollection: IAPGIVPlante[] = [
          {
            ...aPGIVPlante,
          },
          { id: 456 },
        ];
        expectedResult = service.addAPGIVPlanteToCollectionIfMissing(aPGIVPlanteCollection, aPGIVPlante);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a APGIVPlante to an array that doesn't contain it", () => {
        const aPGIVPlante: IAPGIVPlante = { id: 123 };
        const aPGIVPlanteCollection: IAPGIVPlante[] = [{ id: 456 }];
        expectedResult = service.addAPGIVPlanteToCollectionIfMissing(aPGIVPlanteCollection, aPGIVPlante);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aPGIVPlante);
      });

      it('should add only unique APGIVPlante to an array', () => {
        const aPGIVPlanteArray: IAPGIVPlante[] = [{ id: 123 }, { id: 456 }, { id: 10489 }];
        const aPGIVPlanteCollection: IAPGIVPlante[] = [{ id: 123 }];
        expectedResult = service.addAPGIVPlanteToCollectionIfMissing(aPGIVPlanteCollection, ...aPGIVPlanteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aPGIVPlante: IAPGIVPlante = { id: 123 };
        const aPGIVPlante2: IAPGIVPlante = { id: 456 };
        expectedResult = service.addAPGIVPlanteToCollectionIfMissing([], aPGIVPlante, aPGIVPlante2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aPGIVPlante);
        expect(expectedResult).toContain(aPGIVPlante2);
      });

      it('should accept null and undefined values', () => {
        const aPGIVPlante: IAPGIVPlante = { id: 123 };
        expectedResult = service.addAPGIVPlanteToCollectionIfMissing([], null, aPGIVPlante, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aPGIVPlante);
      });

      it('should return initial array if no APGIVPlante is added', () => {
        const aPGIVPlanteCollection: IAPGIVPlante[] = [{ id: 123 }];
        expectedResult = service.addAPGIVPlanteToCollectionIfMissing(aPGIVPlanteCollection, undefined, null);
        expect(expectedResult).toEqual(aPGIVPlanteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
