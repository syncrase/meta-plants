import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRegne, Regne } from '../regne.model';

import { RegneService } from './regne.service';

describe('Regne Service', () => {
  let service: RegneService;
  let httpMock: HttpTestingController;
  let elemDefault: IRegne;
  let expectedResult: IRegne | IRegne[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RegneService);
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

    it('should create a Regne', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Regne()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Regne', () => {
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

    it('should partial update a Regne', () => {
      const patchObject = Object.assign({}, new Regne());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Regne', () => {
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

    it('should delete a Regne', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRegneToCollectionIfMissing', () => {
      it('should add a Regne to an empty array', () => {
        const regne: IRegne = { id: 123 };
        expectedResult = service.addRegneToCollectionIfMissing([], regne);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(regne);
      });

      it('should not add a Regne to an array that contains it', () => {
        const regne: IRegne = { id: 123 };
        const regneCollection: IRegne[] = [
          {
            ...regne,
          },
          { id: 456 },
        ];
        expectedResult = service.addRegneToCollectionIfMissing(regneCollection, regne);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Regne to an array that doesn't contain it", () => {
        const regne: IRegne = { id: 123 };
        const regneCollection: IRegne[] = [{ id: 456 }];
        expectedResult = service.addRegneToCollectionIfMissing(regneCollection, regne);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(regne);
      });

      it('should add only unique Regne to an array', () => {
        const regneArray: IRegne[] = [{ id: 123 }, { id: 456 }, { id: 53879 }];
        const regneCollection: IRegne[] = [{ id: 123 }];
        expectedResult = service.addRegneToCollectionIfMissing(regneCollection, ...regneArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const regne: IRegne = { id: 123 };
        const regne2: IRegne = { id: 456 };
        expectedResult = service.addRegneToCollectionIfMissing([], regne, regne2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(regne);
        expect(expectedResult).toContain(regne2);
      });

      it('should accept null and undefined values', () => {
        const regne: IRegne = { id: 123 };
        expectedResult = service.addRegneToCollectionIfMissing([], null, regne, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(regne);
      });

      it('should return initial array if no Regne is added', () => {
        const regneCollection: IRegne[] = [{ id: 123 }];
        expectedResult = service.addRegneToCollectionIfMissing(regneCollection, undefined, null);
        expect(expectedResult).toEqual(regneCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
