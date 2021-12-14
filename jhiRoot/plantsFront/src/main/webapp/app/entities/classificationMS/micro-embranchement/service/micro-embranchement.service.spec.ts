import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMicroEmbranchement, MicroEmbranchement } from '../micro-embranchement.model';

import { MicroEmbranchementService } from './micro-embranchement.service';

describe('MicroEmbranchement Service', () => {
  let service: MicroEmbranchementService;
  let httpMock: HttpTestingController;
  let elemDefault: IMicroEmbranchement;
  let expectedResult: IMicroEmbranchement | IMicroEmbranchement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MicroEmbranchementService);
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

    it('should create a MicroEmbranchement', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new MicroEmbranchement()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MicroEmbranchement', () => {
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

    it('should partial update a MicroEmbranchement', () => {
      const patchObject = Object.assign(
        {
          nomLatin: 'BBBBBB',
        },
        new MicroEmbranchement()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MicroEmbranchement', () => {
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

    it('should delete a MicroEmbranchement', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMicroEmbranchementToCollectionIfMissing', () => {
      it('should add a MicroEmbranchement to an empty array', () => {
        const microEmbranchement: IMicroEmbranchement = { id: 123 };
        expectedResult = service.addMicroEmbranchementToCollectionIfMissing([], microEmbranchement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(microEmbranchement);
      });

      it('should not add a MicroEmbranchement to an array that contains it', () => {
        const microEmbranchement: IMicroEmbranchement = { id: 123 };
        const microEmbranchementCollection: IMicroEmbranchement[] = [
          {
            ...microEmbranchement,
          },
          { id: 456 },
        ];
        expectedResult = service.addMicroEmbranchementToCollectionIfMissing(microEmbranchementCollection, microEmbranchement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MicroEmbranchement to an array that doesn't contain it", () => {
        const microEmbranchement: IMicroEmbranchement = { id: 123 };
        const microEmbranchementCollection: IMicroEmbranchement[] = [{ id: 456 }];
        expectedResult = service.addMicroEmbranchementToCollectionIfMissing(microEmbranchementCollection, microEmbranchement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(microEmbranchement);
      });

      it('should add only unique MicroEmbranchement to an array', () => {
        const microEmbranchementArray: IMicroEmbranchement[] = [{ id: 123 }, { id: 456 }, { id: 80062 }];
        const microEmbranchementCollection: IMicroEmbranchement[] = [{ id: 123 }];
        expectedResult = service.addMicroEmbranchementToCollectionIfMissing(microEmbranchementCollection, ...microEmbranchementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const microEmbranchement: IMicroEmbranchement = { id: 123 };
        const microEmbranchement2: IMicroEmbranchement = { id: 456 };
        expectedResult = service.addMicroEmbranchementToCollectionIfMissing([], microEmbranchement, microEmbranchement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(microEmbranchement);
        expect(expectedResult).toContain(microEmbranchement2);
      });

      it('should accept null and undefined values', () => {
        const microEmbranchement: IMicroEmbranchement = { id: 123 };
        expectedResult = service.addMicroEmbranchementToCollectionIfMissing([], null, microEmbranchement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(microEmbranchement);
      });

      it('should return initial array if no MicroEmbranchement is added', () => {
        const microEmbranchementCollection: IMicroEmbranchement[] = [{ id: 123 }];
        expectedResult = service.addMicroEmbranchementToCollectionIfMissing(microEmbranchementCollection, undefined, null);
        expect(expectedResult).toEqual(microEmbranchementCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
