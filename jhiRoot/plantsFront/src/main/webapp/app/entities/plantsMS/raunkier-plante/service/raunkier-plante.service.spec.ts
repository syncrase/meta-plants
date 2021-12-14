import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRaunkierPlante, RaunkierPlante } from '../raunkier-plante.model';

import { RaunkierPlanteService } from './raunkier-plante.service';

describe('RaunkierPlante Service', () => {
  let service: RaunkierPlanteService;
  let httpMock: HttpTestingController;
  let elemDefault: IRaunkierPlante;
  let expectedResult: IRaunkierPlante | IRaunkierPlante[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RaunkierPlanteService);
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

    it('should create a RaunkierPlante', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new RaunkierPlante()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RaunkierPlante', () => {
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

    it('should partial update a RaunkierPlante', () => {
      const patchObject = Object.assign(
        {
          type: 'BBBBBB',
        },
        new RaunkierPlante()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RaunkierPlante', () => {
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

    it('should delete a RaunkierPlante', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRaunkierPlanteToCollectionIfMissing', () => {
      it('should add a RaunkierPlante to an empty array', () => {
        const raunkierPlante: IRaunkierPlante = { id: 123 };
        expectedResult = service.addRaunkierPlanteToCollectionIfMissing([], raunkierPlante);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(raunkierPlante);
      });

      it('should not add a RaunkierPlante to an array that contains it', () => {
        const raunkierPlante: IRaunkierPlante = { id: 123 };
        const raunkierPlanteCollection: IRaunkierPlante[] = [
          {
            ...raunkierPlante,
          },
          { id: 456 },
        ];
        expectedResult = service.addRaunkierPlanteToCollectionIfMissing(raunkierPlanteCollection, raunkierPlante);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RaunkierPlante to an array that doesn't contain it", () => {
        const raunkierPlante: IRaunkierPlante = { id: 123 };
        const raunkierPlanteCollection: IRaunkierPlante[] = [{ id: 456 }];
        expectedResult = service.addRaunkierPlanteToCollectionIfMissing(raunkierPlanteCollection, raunkierPlante);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(raunkierPlante);
      });

      it('should add only unique RaunkierPlante to an array', () => {
        const raunkierPlanteArray: IRaunkierPlante[] = [{ id: 123 }, { id: 456 }, { id: 2158 }];
        const raunkierPlanteCollection: IRaunkierPlante[] = [{ id: 123 }];
        expectedResult = service.addRaunkierPlanteToCollectionIfMissing(raunkierPlanteCollection, ...raunkierPlanteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const raunkierPlante: IRaunkierPlante = { id: 123 };
        const raunkierPlante2: IRaunkierPlante = { id: 456 };
        expectedResult = service.addRaunkierPlanteToCollectionIfMissing([], raunkierPlante, raunkierPlante2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(raunkierPlante);
        expect(expectedResult).toContain(raunkierPlante2);
      });

      it('should accept null and undefined values', () => {
        const raunkierPlante: IRaunkierPlante = { id: 123 };
        expectedResult = service.addRaunkierPlanteToCollectionIfMissing([], null, raunkierPlante, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(raunkierPlante);
      });

      it('should return initial array if no RaunkierPlante is added', () => {
        const raunkierPlanteCollection: IRaunkierPlante[] = [{ id: 123 }];
        expectedResult = service.addRaunkierPlanteToCollectionIfMissing(raunkierPlanteCollection, undefined, null);
        expect(expectedResult).toEqual(raunkierPlanteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
