import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISuperClasse, SuperClasse } from '../super-classe.model';

import { SuperClasseService } from './super-classe.service';

describe('SuperClasse Service', () => {
  let service: SuperClasseService;
  let httpMock: HttpTestingController;
  let elemDefault: ISuperClasse;
  let expectedResult: ISuperClasse | ISuperClasse[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SuperClasseService);
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

    it('should create a SuperClasse', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SuperClasse()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SuperClasse', () => {
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

    it('should partial update a SuperClasse', () => {
      const patchObject = Object.assign({}, new SuperClasse());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SuperClasse', () => {
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

    it('should delete a SuperClasse', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSuperClasseToCollectionIfMissing', () => {
      it('should add a SuperClasse to an empty array', () => {
        const superClasse: ISuperClasse = { id: 123 };
        expectedResult = service.addSuperClasseToCollectionIfMissing([], superClasse);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(superClasse);
      });

      it('should not add a SuperClasse to an array that contains it', () => {
        const superClasse: ISuperClasse = { id: 123 };
        const superClasseCollection: ISuperClasse[] = [
          {
            ...superClasse,
          },
          { id: 456 },
        ];
        expectedResult = service.addSuperClasseToCollectionIfMissing(superClasseCollection, superClasse);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SuperClasse to an array that doesn't contain it", () => {
        const superClasse: ISuperClasse = { id: 123 };
        const superClasseCollection: ISuperClasse[] = [{ id: 456 }];
        expectedResult = service.addSuperClasseToCollectionIfMissing(superClasseCollection, superClasse);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(superClasse);
      });

      it('should add only unique SuperClasse to an array', () => {
        const superClasseArray: ISuperClasse[] = [{ id: 123 }, { id: 456 }, { id: 56057 }];
        const superClasseCollection: ISuperClasse[] = [{ id: 123 }];
        expectedResult = service.addSuperClasseToCollectionIfMissing(superClasseCollection, ...superClasseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const superClasse: ISuperClasse = { id: 123 };
        const superClasse2: ISuperClasse = { id: 456 };
        expectedResult = service.addSuperClasseToCollectionIfMissing([], superClasse, superClasse2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(superClasse);
        expect(expectedResult).toContain(superClasse2);
      });

      it('should accept null and undefined values', () => {
        const superClasse: ISuperClasse = { id: 123 };
        expectedResult = service.addSuperClasseToCollectionIfMissing([], null, superClasse, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(superClasse);
      });

      it('should return initial array if no SuperClasse is added', () => {
        const superClasseCollection: ISuperClasse[] = [{ id: 123 }];
        expectedResult = service.addSuperClasseToCollectionIfMissing(superClasseCollection, undefined, null);
        expect(expectedResult).toEqual(superClasseCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
