import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IForme, Forme } from '../forme.model';

import { FormeService } from './forme.service';

describe('Forme Service', () => {
  let service: FormeService;
  let httpMock: HttpTestingController;
  let elemDefault: IForme;
  let expectedResult: IForme | IForme[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FormeService);
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

    it('should create a Forme', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Forme()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Forme', () => {
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

    it('should partial update a Forme', () => {
      const patchObject = Object.assign(
        {
          nomFr: 'BBBBBB',
          nomLatin: 'BBBBBB',
        },
        new Forme()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Forme', () => {
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

    it('should delete a Forme', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFormeToCollectionIfMissing', () => {
      it('should add a Forme to an empty array', () => {
        const forme: IForme = { id: 123 };
        expectedResult = service.addFormeToCollectionIfMissing([], forme);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(forme);
      });

      it('should not add a Forme to an array that contains it', () => {
        const forme: IForme = { id: 123 };
        const formeCollection: IForme[] = [
          {
            ...forme,
          },
          { id: 456 },
        ];
        expectedResult = service.addFormeToCollectionIfMissing(formeCollection, forme);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Forme to an array that doesn't contain it", () => {
        const forme: IForme = { id: 123 };
        const formeCollection: IForme[] = [{ id: 456 }];
        expectedResult = service.addFormeToCollectionIfMissing(formeCollection, forme);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(forme);
      });

      it('should add only unique Forme to an array', () => {
        const formeArray: IForme[] = [{ id: 123 }, { id: 456 }, { id: 61532 }];
        const formeCollection: IForme[] = [{ id: 123 }];
        expectedResult = service.addFormeToCollectionIfMissing(formeCollection, ...formeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const forme: IForme = { id: 123 };
        const forme2: IForme = { id: 456 };
        expectedResult = service.addFormeToCollectionIfMissing([], forme, forme2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(forme);
        expect(expectedResult).toContain(forme2);
      });

      it('should accept null and undefined values', () => {
        const forme: IForme = { id: 123 };
        expectedResult = service.addFormeToCollectionIfMissing([], null, forme, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(forme);
      });

      it('should return initial array if no Forme is added', () => {
        const formeCollection: IForme[] = [{ id: 123 }];
        expectedResult = service.addFormeToCollectionIfMissing(formeCollection, undefined, null);
        expect(expectedResult).toEqual(formeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
