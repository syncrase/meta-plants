import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAPGIIIPlante, APGIIIPlante } from '../apgiii-plante.model';

import { APGIIIPlanteService } from './apgiii-plante.service';

describe('APGIIIPlante Service', () => {
  let service: APGIIIPlanteService;
  let httpMock: HttpTestingController;
  let elemDefault: IAPGIIIPlante;
  let expectedResult: IAPGIIIPlante | IAPGIIIPlante[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(APGIIIPlanteService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      ordre: 'AAAAAAA',
      famille: 'AAAAAAA',
      sousFamille: 'AAAAAAA',
      tribu: 'AAAAAAA',
      sousTribu: 'AAAAAAA',
      genre: 'AAAAAAA',
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

    it('should create a APGIIIPlante', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new APGIIIPlante()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a APGIIIPlante', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          ordre: 'BBBBBB',
          famille: 'BBBBBB',
          sousFamille: 'BBBBBB',
          tribu: 'BBBBBB',
          sousTribu: 'BBBBBB',
          genre: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a APGIIIPlante', () => {
      const patchObject = Object.assign(
        {
          famille: 'BBBBBB',
          genre: 'BBBBBB',
        },
        new APGIIIPlante()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of APGIIIPlante', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          ordre: 'BBBBBB',
          famille: 'BBBBBB',
          sousFamille: 'BBBBBB',
          tribu: 'BBBBBB',
          sousTribu: 'BBBBBB',
          genre: 'BBBBBB',
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

    it('should delete a APGIIIPlante', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAPGIIIPlanteToCollectionIfMissing', () => {
      it('should add a APGIIIPlante to an empty array', () => {
        const aPGIIIPlante: IAPGIIIPlante = { id: 123 };
        expectedResult = service.addAPGIIIPlanteToCollectionIfMissing([], aPGIIIPlante);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aPGIIIPlante);
      });

      it('should not add a APGIIIPlante to an array that contains it', () => {
        const aPGIIIPlante: IAPGIIIPlante = { id: 123 };
        const aPGIIIPlanteCollection: IAPGIIIPlante[] = [
          {
            ...aPGIIIPlante,
          },
          { id: 456 },
        ];
        expectedResult = service.addAPGIIIPlanteToCollectionIfMissing(aPGIIIPlanteCollection, aPGIIIPlante);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a APGIIIPlante to an array that doesn't contain it", () => {
        const aPGIIIPlante: IAPGIIIPlante = { id: 123 };
        const aPGIIIPlanteCollection: IAPGIIIPlante[] = [{ id: 456 }];
        expectedResult = service.addAPGIIIPlanteToCollectionIfMissing(aPGIIIPlanteCollection, aPGIIIPlante);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aPGIIIPlante);
      });

      it('should add only unique APGIIIPlante to an array', () => {
        const aPGIIIPlanteArray: IAPGIIIPlante[] = [{ id: 123 }, { id: 456 }, { id: 18658 }];
        const aPGIIIPlanteCollection: IAPGIIIPlante[] = [{ id: 123 }];
        expectedResult = service.addAPGIIIPlanteToCollectionIfMissing(aPGIIIPlanteCollection, ...aPGIIIPlanteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const aPGIIIPlante: IAPGIIIPlante = { id: 123 };
        const aPGIIIPlante2: IAPGIIIPlante = { id: 456 };
        expectedResult = service.addAPGIIIPlanteToCollectionIfMissing([], aPGIIIPlante, aPGIIIPlante2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(aPGIIIPlante);
        expect(expectedResult).toContain(aPGIIIPlante2);
      });

      it('should accept null and undefined values', () => {
        const aPGIIIPlante: IAPGIIIPlante = { id: 123 };
        expectedResult = service.addAPGIIIPlanteToCollectionIfMissing([], null, aPGIIIPlante, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(aPGIIIPlante);
      });

      it('should return initial array if no APGIIIPlante is added', () => {
        const aPGIIIPlanteCollection: IAPGIIIPlante[] = [{ id: 123 }];
        expectedResult = service.addAPGIIIPlanteToCollectionIfMissing(aPGIIIPlanteCollection, undefined, null);
        expect(expectedResult).toEqual(aPGIIIPlanteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
