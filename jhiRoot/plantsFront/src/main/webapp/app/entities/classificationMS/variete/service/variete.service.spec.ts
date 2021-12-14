import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVariete, Variete } from '../variete.model';

import { VarieteService } from './variete.service';

describe('Variete Service', () => {
  let service: VarieteService;
  let httpMock: HttpTestingController;
  let elemDefault: IVariete;
  let expectedResult: IVariete | IVariete[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VarieteService);
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

    it('should create a Variete', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Variete()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Variete', () => {
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

    it('should partial update a Variete', () => {
      const patchObject = Object.assign(
        {
          nomFr: 'BBBBBB',
        },
        new Variete()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Variete', () => {
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

    it('should delete a Variete', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVarieteToCollectionIfMissing', () => {
      it('should add a Variete to an empty array', () => {
        const variete: IVariete = { id: 123 };
        expectedResult = service.addVarieteToCollectionIfMissing([], variete);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(variete);
      });

      it('should not add a Variete to an array that contains it', () => {
        const variete: IVariete = { id: 123 };
        const varieteCollection: IVariete[] = [
          {
            ...variete,
          },
          { id: 456 },
        ];
        expectedResult = service.addVarieteToCollectionIfMissing(varieteCollection, variete);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Variete to an array that doesn't contain it", () => {
        const variete: IVariete = { id: 123 };
        const varieteCollection: IVariete[] = [{ id: 456 }];
        expectedResult = service.addVarieteToCollectionIfMissing(varieteCollection, variete);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(variete);
      });

      it('should add only unique Variete to an array', () => {
        const varieteArray: IVariete[] = [{ id: 123 }, { id: 456 }, { id: 73307 }];
        const varieteCollection: IVariete[] = [{ id: 123 }];
        expectedResult = service.addVarieteToCollectionIfMissing(varieteCollection, ...varieteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const variete: IVariete = { id: 123 };
        const variete2: IVariete = { id: 456 };
        expectedResult = service.addVarieteToCollectionIfMissing([], variete, variete2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(variete);
        expect(expectedResult).toContain(variete2);
      });

      it('should accept null and undefined values', () => {
        const variete: IVariete = { id: 123 };
        expectedResult = service.addVarieteToCollectionIfMissing([], null, variete, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(variete);
      });

      it('should return initial array if no Variete is added', () => {
        const varieteCollection: IVariete[] = [{ id: 123 }];
        expectedResult = service.addVarieteToCollectionIfMissing(varieteCollection, undefined, null);
        expect(expectedResult).toEqual(varieteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
