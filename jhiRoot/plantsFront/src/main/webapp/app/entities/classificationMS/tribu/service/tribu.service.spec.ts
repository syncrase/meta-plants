import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITribu, Tribu } from '../tribu.model';

import { TribuService } from './tribu.service';

describe('Tribu Service', () => {
  let service: TribuService;
  let httpMock: HttpTestingController;
  let elemDefault: ITribu;
  let expectedResult: ITribu | ITribu[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TribuService);
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

    it('should create a Tribu', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Tribu()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Tribu', () => {
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

    it('should partial update a Tribu', () => {
      const patchObject = Object.assign({}, new Tribu());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Tribu', () => {
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

    it('should delete a Tribu', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTribuToCollectionIfMissing', () => {
      it('should add a Tribu to an empty array', () => {
        const tribu: ITribu = { id: 123 };
        expectedResult = service.addTribuToCollectionIfMissing([], tribu);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tribu);
      });

      it('should not add a Tribu to an array that contains it', () => {
        const tribu: ITribu = { id: 123 };
        const tribuCollection: ITribu[] = [
          {
            ...tribu,
          },
          { id: 456 },
        ];
        expectedResult = service.addTribuToCollectionIfMissing(tribuCollection, tribu);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Tribu to an array that doesn't contain it", () => {
        const tribu: ITribu = { id: 123 };
        const tribuCollection: ITribu[] = [{ id: 456 }];
        expectedResult = service.addTribuToCollectionIfMissing(tribuCollection, tribu);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tribu);
      });

      it('should add only unique Tribu to an array', () => {
        const tribuArray: ITribu[] = [{ id: 123 }, { id: 456 }, { id: 16854 }];
        const tribuCollection: ITribu[] = [{ id: 123 }];
        expectedResult = service.addTribuToCollectionIfMissing(tribuCollection, ...tribuArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tribu: ITribu = { id: 123 };
        const tribu2: ITribu = { id: 456 };
        expectedResult = service.addTribuToCollectionIfMissing([], tribu, tribu2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tribu);
        expect(expectedResult).toContain(tribu2);
      });

      it('should accept null and undefined values', () => {
        const tribu: ITribu = { id: 123 };
        expectedResult = service.addTribuToCollectionIfMissing([], null, tribu, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tribu);
      });

      it('should return initial array if no Tribu is added', () => {
        const tribuCollection: ITribu[] = [{ id: 123 }];
        expectedResult = service.addTribuToCollectionIfMissing(tribuCollection, undefined, null);
        expect(expectedResult).toEqual(tribuCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
