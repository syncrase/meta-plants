import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISuperRegne, SuperRegne } from '../super-regne.model';

import { SuperRegneService } from './super-regne.service';

describe('SuperRegne Service', () => {
  let service: SuperRegneService;
  let httpMock: HttpTestingController;
  let elemDefault: ISuperRegne;
  let expectedResult: ISuperRegne | ISuperRegne[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SuperRegneService);
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

    it('should create a SuperRegne', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SuperRegne()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SuperRegne', () => {
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

    it('should partial update a SuperRegne', () => {
      const patchObject = Object.assign(
        {
          nomLatin: 'BBBBBB',
        },
        new SuperRegne()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SuperRegne', () => {
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

    it('should delete a SuperRegne', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSuperRegneToCollectionIfMissing', () => {
      it('should add a SuperRegne to an empty array', () => {
        const superRegne: ISuperRegne = { id: 123 };
        expectedResult = service.addSuperRegneToCollectionIfMissing([], superRegne);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(superRegne);
      });

      it('should not add a SuperRegne to an array that contains it', () => {
        const superRegne: ISuperRegne = { id: 123 };
        const superRegneCollection: ISuperRegne[] = [
          {
            ...superRegne,
          },
          { id: 456 },
        ];
        expectedResult = service.addSuperRegneToCollectionIfMissing(superRegneCollection, superRegne);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SuperRegne to an array that doesn't contain it", () => {
        const superRegne: ISuperRegne = { id: 123 };
        const superRegneCollection: ISuperRegne[] = [{ id: 456 }];
        expectedResult = service.addSuperRegneToCollectionIfMissing(superRegneCollection, superRegne);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(superRegne);
      });

      it('should add only unique SuperRegne to an array', () => {
        const superRegneArray: ISuperRegne[] = [{ id: 123 }, { id: 456 }, { id: 74770 }];
        const superRegneCollection: ISuperRegne[] = [{ id: 123 }];
        expectedResult = service.addSuperRegneToCollectionIfMissing(superRegneCollection, ...superRegneArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const superRegne: ISuperRegne = { id: 123 };
        const superRegne2: ISuperRegne = { id: 456 };
        expectedResult = service.addSuperRegneToCollectionIfMissing([], superRegne, superRegne2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(superRegne);
        expect(expectedResult).toContain(superRegne2);
      });

      it('should accept null and undefined values', () => {
        const superRegne: ISuperRegne = { id: 123 };
        expectedResult = service.addSuperRegneToCollectionIfMissing([], null, superRegne, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(superRegne);
      });

      it('should return initial array if no SuperRegne is added', () => {
        const superRegneCollection: ISuperRegne[] = [{ id: 123 }];
        expectedResult = service.addSuperRegneToCollectionIfMissing(superRegneCollection, undefined, null);
        expect(expectedResult).toEqual(superRegneCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
