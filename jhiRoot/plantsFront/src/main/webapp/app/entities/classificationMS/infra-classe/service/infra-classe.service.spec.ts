import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInfraClasse, InfraClasse } from '../infra-classe.model';

import { InfraClasseService } from './infra-classe.service';

describe('InfraClasse Service', () => {
  let service: InfraClasseService;
  let httpMock: HttpTestingController;
  let elemDefault: IInfraClasse;
  let expectedResult: IInfraClasse | IInfraClasse[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InfraClasseService);
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

    it('should create a InfraClasse', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new InfraClasse()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InfraClasse', () => {
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

    it('should partial update a InfraClasse', () => {
      const patchObject = Object.assign(
        {
          nomFr: 'BBBBBB',
        },
        new InfraClasse()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InfraClasse', () => {
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

    it('should delete a InfraClasse', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInfraClasseToCollectionIfMissing', () => {
      it('should add a InfraClasse to an empty array', () => {
        const infraClasse: IInfraClasse = { id: 123 };
        expectedResult = service.addInfraClasseToCollectionIfMissing([], infraClasse);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(infraClasse);
      });

      it('should not add a InfraClasse to an array that contains it', () => {
        const infraClasse: IInfraClasse = { id: 123 };
        const infraClasseCollection: IInfraClasse[] = [
          {
            ...infraClasse,
          },
          { id: 456 },
        ];
        expectedResult = service.addInfraClasseToCollectionIfMissing(infraClasseCollection, infraClasse);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InfraClasse to an array that doesn't contain it", () => {
        const infraClasse: IInfraClasse = { id: 123 };
        const infraClasseCollection: IInfraClasse[] = [{ id: 456 }];
        expectedResult = service.addInfraClasseToCollectionIfMissing(infraClasseCollection, infraClasse);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(infraClasse);
      });

      it('should add only unique InfraClasse to an array', () => {
        const infraClasseArray: IInfraClasse[] = [{ id: 123 }, { id: 456 }, { id: 66419 }];
        const infraClasseCollection: IInfraClasse[] = [{ id: 123 }];
        expectedResult = service.addInfraClasseToCollectionIfMissing(infraClasseCollection, ...infraClasseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const infraClasse: IInfraClasse = { id: 123 };
        const infraClasse2: IInfraClasse = { id: 456 };
        expectedResult = service.addInfraClasseToCollectionIfMissing([], infraClasse, infraClasse2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(infraClasse);
        expect(expectedResult).toContain(infraClasse2);
      });

      it('should accept null and undefined values', () => {
        const infraClasse: IInfraClasse = { id: 123 };
        expectedResult = service.addInfraClasseToCollectionIfMissing([], null, infraClasse, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(infraClasse);
      });

      it('should return initial array if no InfraClasse is added', () => {
        const infraClasseCollection: IInfraClasse[] = [{ id: 123 }];
        expectedResult = service.addInfraClasseToCollectionIfMissing(infraClasseCollection, undefined, null);
        expect(expectedResult).toEqual(infraClasseCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
