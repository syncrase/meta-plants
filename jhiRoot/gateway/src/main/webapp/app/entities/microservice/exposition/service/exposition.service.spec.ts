import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IExposition, Exposition } from '../exposition.model';

import { ExpositionService } from './exposition.service';

describe('Exposition Service', () => {
  let service: ExpositionService;
  let httpMock: HttpTestingController;
  let elemDefault: IExposition;
  let expectedResult: IExposition | IExposition[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ExpositionService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      valeur: 'AAAAAAA',
      ensoleilement: 0,
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

    it('should create a Exposition', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Exposition()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Exposition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          valeur: 'BBBBBB',
          ensoleilement: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Exposition', () => {
      const patchObject = Object.assign(
        {
          valeur: 'BBBBBB',
          ensoleilement: 1,
        },
        new Exposition()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Exposition', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          valeur: 'BBBBBB',
          ensoleilement: 1,
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

    it('should delete a Exposition', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addExpositionToCollectionIfMissing', () => {
      it('should add a Exposition to an empty array', () => {
        const exposition: IExposition = { id: 123 };
        expectedResult = service.addExpositionToCollectionIfMissing([], exposition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(exposition);
      });

      it('should not add a Exposition to an array that contains it', () => {
        const exposition: IExposition = { id: 123 };
        const expositionCollection: IExposition[] = [
          {
            ...exposition,
          },
          { id: 456 },
        ];
        expectedResult = service.addExpositionToCollectionIfMissing(expositionCollection, exposition);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Exposition to an array that doesn't contain it", () => {
        const exposition: IExposition = { id: 123 };
        const expositionCollection: IExposition[] = [{ id: 456 }];
        expectedResult = service.addExpositionToCollectionIfMissing(expositionCollection, exposition);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(exposition);
      });

      it('should add only unique Exposition to an array', () => {
        const expositionArray: IExposition[] = [{ id: 123 }, { id: 456 }, { id: 243 }];
        const expositionCollection: IExposition[] = [{ id: 123 }];
        expectedResult = service.addExpositionToCollectionIfMissing(expositionCollection, ...expositionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const exposition: IExposition = { id: 123 };
        const exposition2: IExposition = { id: 456 };
        expectedResult = service.addExpositionToCollectionIfMissing([], exposition, exposition2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(exposition);
        expect(expectedResult).toContain(exposition2);
      });

      it('should accept null and undefined values', () => {
        const exposition: IExposition = { id: 123 };
        expectedResult = service.addExpositionToCollectionIfMissing([], null, exposition, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(exposition);
      });

      it('should return initial array if no Exposition is added', () => {
        const expositionCollection: IExposition[] = [{ id: 123 }];
        expectedResult = service.addExpositionToCollectionIfMissing(expositionCollection, undefined, null);
        expect(expectedResult).toEqual(expositionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
