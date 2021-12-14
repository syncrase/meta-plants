import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISuperFamille, SuperFamille } from '../super-famille.model';

import { SuperFamilleService } from './super-famille.service';

describe('SuperFamille Service', () => {
  let service: SuperFamilleService;
  let httpMock: HttpTestingController;
  let elemDefault: ISuperFamille;
  let expectedResult: ISuperFamille | ISuperFamille[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SuperFamilleService);
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

    it('should create a SuperFamille', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SuperFamille()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SuperFamille', () => {
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

    it('should partial update a SuperFamille', () => {
      const patchObject = Object.assign(
        {
          nomFr: 'BBBBBB',
          nomLatin: 'BBBBBB',
        },
        new SuperFamille()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SuperFamille', () => {
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

    it('should delete a SuperFamille', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSuperFamilleToCollectionIfMissing', () => {
      it('should add a SuperFamille to an empty array', () => {
        const superFamille: ISuperFamille = { id: 123 };
        expectedResult = service.addSuperFamilleToCollectionIfMissing([], superFamille);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(superFamille);
      });

      it('should not add a SuperFamille to an array that contains it', () => {
        const superFamille: ISuperFamille = { id: 123 };
        const superFamilleCollection: ISuperFamille[] = [
          {
            ...superFamille,
          },
          { id: 456 },
        ];
        expectedResult = service.addSuperFamilleToCollectionIfMissing(superFamilleCollection, superFamille);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SuperFamille to an array that doesn't contain it", () => {
        const superFamille: ISuperFamille = { id: 123 };
        const superFamilleCollection: ISuperFamille[] = [{ id: 456 }];
        expectedResult = service.addSuperFamilleToCollectionIfMissing(superFamilleCollection, superFamille);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(superFamille);
      });

      it('should add only unique SuperFamille to an array', () => {
        const superFamilleArray: ISuperFamille[] = [{ id: 123 }, { id: 456 }, { id: 12889 }];
        const superFamilleCollection: ISuperFamille[] = [{ id: 123 }];
        expectedResult = service.addSuperFamilleToCollectionIfMissing(superFamilleCollection, ...superFamilleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const superFamille: ISuperFamille = { id: 123 };
        const superFamille2: ISuperFamille = { id: 456 };
        expectedResult = service.addSuperFamilleToCollectionIfMissing([], superFamille, superFamille2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(superFamille);
        expect(expectedResult).toContain(superFamille2);
      });

      it('should accept null and undefined values', () => {
        const superFamille: ISuperFamille = { id: 123 };
        expectedResult = service.addSuperFamilleToCollectionIfMissing([], null, superFamille, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(superFamille);
      });

      it('should return initial array if no SuperFamille is added', () => {
        const superFamilleCollection: ISuperFamille[] = [{ id: 123 }];
        expectedResult = service.addSuperFamilleToCollectionIfMissing(superFamilleCollection, undefined, null);
        expect(expectedResult).toEqual(superFamilleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
