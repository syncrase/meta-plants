import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInfraEmbranchement, InfraEmbranchement } from '../infra-embranchement.model';

import { InfraEmbranchementService } from './infra-embranchement.service';

describe('InfraEmbranchement Service', () => {
  let service: InfraEmbranchementService;
  let httpMock: HttpTestingController;
  let elemDefault: IInfraEmbranchement;
  let expectedResult: IInfraEmbranchement | IInfraEmbranchement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InfraEmbranchementService);
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

    it('should create a InfraEmbranchement', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new InfraEmbranchement()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InfraEmbranchement', () => {
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

    it('should partial update a InfraEmbranchement', () => {
      const patchObject = Object.assign(
        {
          nomFr: 'BBBBBB',
        },
        new InfraEmbranchement()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InfraEmbranchement', () => {
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

    it('should delete a InfraEmbranchement', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInfraEmbranchementToCollectionIfMissing', () => {
      it('should add a InfraEmbranchement to an empty array', () => {
        const infraEmbranchement: IInfraEmbranchement = { id: 123 };
        expectedResult = service.addInfraEmbranchementToCollectionIfMissing([], infraEmbranchement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(infraEmbranchement);
      });

      it('should not add a InfraEmbranchement to an array that contains it', () => {
        const infraEmbranchement: IInfraEmbranchement = { id: 123 };
        const infraEmbranchementCollection: IInfraEmbranchement[] = [
          {
            ...infraEmbranchement,
          },
          { id: 456 },
        ];
        expectedResult = service.addInfraEmbranchementToCollectionIfMissing(infraEmbranchementCollection, infraEmbranchement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InfraEmbranchement to an array that doesn't contain it", () => {
        const infraEmbranchement: IInfraEmbranchement = { id: 123 };
        const infraEmbranchementCollection: IInfraEmbranchement[] = [{ id: 456 }];
        expectedResult = service.addInfraEmbranchementToCollectionIfMissing(infraEmbranchementCollection, infraEmbranchement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(infraEmbranchement);
      });

      it('should add only unique InfraEmbranchement to an array', () => {
        const infraEmbranchementArray: IInfraEmbranchement[] = [{ id: 123 }, { id: 456 }, { id: 10939 }];
        const infraEmbranchementCollection: IInfraEmbranchement[] = [{ id: 123 }];
        expectedResult = service.addInfraEmbranchementToCollectionIfMissing(infraEmbranchementCollection, ...infraEmbranchementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const infraEmbranchement: IInfraEmbranchement = { id: 123 };
        const infraEmbranchement2: IInfraEmbranchement = { id: 456 };
        expectedResult = service.addInfraEmbranchementToCollectionIfMissing([], infraEmbranchement, infraEmbranchement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(infraEmbranchement);
        expect(expectedResult).toContain(infraEmbranchement2);
      });

      it('should accept null and undefined values', () => {
        const infraEmbranchement: IInfraEmbranchement = { id: 123 };
        expectedResult = service.addInfraEmbranchementToCollectionIfMissing([], null, infraEmbranchement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(infraEmbranchement);
      });

      it('should return initial array if no InfraEmbranchement is added', () => {
        const infraEmbranchementCollection: IInfraEmbranchement[] = [{ id: 123 }];
        expectedResult = service.addInfraEmbranchementToCollectionIfMissing(infraEmbranchementCollection, undefined, null);
        expect(expectedResult).toEqual(infraEmbranchementCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
