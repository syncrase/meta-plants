import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInfraRegne, InfraRegne } from '../infra-regne.model';

import { InfraRegneService } from './infra-regne.service';

describe('InfraRegne Service', () => {
  let service: InfraRegneService;
  let httpMock: HttpTestingController;
  let elemDefault: IInfraRegne;
  let expectedResult: IInfraRegne | IInfraRegne[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InfraRegneService);
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

    it('should create a InfraRegne', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new InfraRegne()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InfraRegne', () => {
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

    it('should partial update a InfraRegne', () => {
      const patchObject = Object.assign(
        {
          nomLatin: 'BBBBBB',
        },
        new InfraRegne()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InfraRegne', () => {
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

    it('should delete a InfraRegne', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInfraRegneToCollectionIfMissing', () => {
      it('should add a InfraRegne to an empty array', () => {
        const infraRegne: IInfraRegne = { id: 123 };
        expectedResult = service.addInfraRegneToCollectionIfMissing([], infraRegne);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(infraRegne);
      });

      it('should not add a InfraRegne to an array that contains it', () => {
        const infraRegne: IInfraRegne = { id: 123 };
        const infraRegneCollection: IInfraRegne[] = [
          {
            ...infraRegne,
          },
          { id: 456 },
        ];
        expectedResult = service.addInfraRegneToCollectionIfMissing(infraRegneCollection, infraRegne);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InfraRegne to an array that doesn't contain it", () => {
        const infraRegne: IInfraRegne = { id: 123 };
        const infraRegneCollection: IInfraRegne[] = [{ id: 456 }];
        expectedResult = service.addInfraRegneToCollectionIfMissing(infraRegneCollection, infraRegne);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(infraRegne);
      });

      it('should add only unique InfraRegne to an array', () => {
        const infraRegneArray: IInfraRegne[] = [{ id: 123 }, { id: 456 }, { id: 13703 }];
        const infraRegneCollection: IInfraRegne[] = [{ id: 123 }];
        expectedResult = service.addInfraRegneToCollectionIfMissing(infraRegneCollection, ...infraRegneArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const infraRegne: IInfraRegne = { id: 123 };
        const infraRegne2: IInfraRegne = { id: 456 };
        expectedResult = service.addInfraRegneToCollectionIfMissing([], infraRegne, infraRegne2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(infraRegne);
        expect(expectedResult).toContain(infraRegne2);
      });

      it('should accept null and undefined values', () => {
        const infraRegne: IInfraRegne = { id: 123 };
        expectedResult = service.addInfraRegneToCollectionIfMissing([], null, infraRegne, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(infraRegne);
      });

      it('should return initial array if no InfraRegne is added', () => {
        const infraRegneCollection: IInfraRegne[] = [{ id: 123 }];
        expectedResult = service.addInfraRegneToCollectionIfMissing(infraRegneCollection, undefined, null);
        expect(expectedResult).toEqual(infraRegneCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
