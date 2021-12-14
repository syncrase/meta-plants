import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISousClasse, SousClasse } from '../sous-classe.model';

import { SousClasseService } from './sous-classe.service';

describe('SousClasse Service', () => {
  let service: SousClasseService;
  let httpMock: HttpTestingController;
  let elemDefault: ISousClasse;
  let expectedResult: ISousClasse | ISousClasse[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SousClasseService);
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

    it('should create a SousClasse', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SousClasse()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SousClasse', () => {
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

    it('should partial update a SousClasse', () => {
      const patchObject = Object.assign(
        {
          nomFr: 'BBBBBB',
        },
        new SousClasse()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SousClasse', () => {
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

    it('should delete a SousClasse', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSousClasseToCollectionIfMissing', () => {
      it('should add a SousClasse to an empty array', () => {
        const sousClasse: ISousClasse = { id: 123 };
        expectedResult = service.addSousClasseToCollectionIfMissing([], sousClasse);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousClasse);
      });

      it('should not add a SousClasse to an array that contains it', () => {
        const sousClasse: ISousClasse = { id: 123 };
        const sousClasseCollection: ISousClasse[] = [
          {
            ...sousClasse,
          },
          { id: 456 },
        ];
        expectedResult = service.addSousClasseToCollectionIfMissing(sousClasseCollection, sousClasse);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SousClasse to an array that doesn't contain it", () => {
        const sousClasse: ISousClasse = { id: 123 };
        const sousClasseCollection: ISousClasse[] = [{ id: 456 }];
        expectedResult = service.addSousClasseToCollectionIfMissing(sousClasseCollection, sousClasse);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousClasse);
      });

      it('should add only unique SousClasse to an array', () => {
        const sousClasseArray: ISousClasse[] = [{ id: 123 }, { id: 456 }, { id: 1350 }];
        const sousClasseCollection: ISousClasse[] = [{ id: 123 }];
        expectedResult = service.addSousClasseToCollectionIfMissing(sousClasseCollection, ...sousClasseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sousClasse: ISousClasse = { id: 123 };
        const sousClasse2: ISousClasse = { id: 456 };
        expectedResult = service.addSousClasseToCollectionIfMissing([], sousClasse, sousClasse2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousClasse);
        expect(expectedResult).toContain(sousClasse2);
      });

      it('should accept null and undefined values', () => {
        const sousClasse: ISousClasse = { id: 123 };
        expectedResult = service.addSousClasseToCollectionIfMissing([], null, sousClasse, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousClasse);
      });

      it('should return initial array if no SousClasse is added', () => {
        const sousClasseCollection: ISousClasse[] = [{ id: 123 }];
        expectedResult = service.addSousClasseToCollectionIfMissing(sousClasseCollection, undefined, null);
        expect(expectedResult).toEqual(sousClasseCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
