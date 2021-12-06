import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICronquist, Cronquist } from '../cronquist.model';

import { CronquistService } from './cronquist.service';

describe('Cronquist Service', () => {
  let service: CronquistService;
  let httpMock: HttpTestingController;
  let elemDefault: ICronquist;
  let expectedResult: ICronquist | ICronquist[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CronquistService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      regne: 'AAAAAAA',
      sousRegne: 'AAAAAAA',
      division: 'AAAAAAA',
      classe: 'AAAAAAA',
      sousClasse: 'AAAAAAA',
      ordre: 'AAAAAAA',
      famille: 'AAAAAAA',
      genre: 'AAAAAAA',
      espece: 'AAAAAAA',
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

    it('should create a Cronquist', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Cronquist()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cronquist', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          regne: 'BBBBBB',
          sousRegne: 'BBBBBB',
          division: 'BBBBBB',
          classe: 'BBBBBB',
          sousClasse: 'BBBBBB',
          ordre: 'BBBBBB',
          famille: 'BBBBBB',
          genre: 'BBBBBB',
          espece: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cronquist', () => {
      const patchObject = Object.assign(
        {
          sousRegne: 'BBBBBB',
          classe: 'BBBBBB',
          famille: 'BBBBBB',
        },
        new Cronquist()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cronquist', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          regne: 'BBBBBB',
          sousRegne: 'BBBBBB',
          division: 'BBBBBB',
          classe: 'BBBBBB',
          sousClasse: 'BBBBBB',
          ordre: 'BBBBBB',
          famille: 'BBBBBB',
          genre: 'BBBBBB',
          espece: 'BBBBBB',
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

    it('should delete a Cronquist', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCronquistToCollectionIfMissing', () => {
      it('should add a Cronquist to an empty array', () => {
        const cronquist: ICronquist = { id: 123 };
        expectedResult = service.addCronquistToCollectionIfMissing([], cronquist);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cronquist);
      });

      it('should not add a Cronquist to an array that contains it', () => {
        const cronquist: ICronquist = { id: 123 };
        const cronquistCollection: ICronquist[] = [
          {
            ...cronquist,
          },
          { id: 456 },
        ];
        expectedResult = service.addCronquistToCollectionIfMissing(cronquistCollection, cronquist);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cronquist to an array that doesn't contain it", () => {
        const cronquist: ICronquist = { id: 123 };
        const cronquistCollection: ICronquist[] = [{ id: 456 }];
        expectedResult = service.addCronquistToCollectionIfMissing(cronquistCollection, cronquist);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cronquist);
      });

      it('should add only unique Cronquist to an array', () => {
        const cronquistArray: ICronquist[] = [{ id: 123 }, { id: 456 }, { id: 1979 }];
        const cronquistCollection: ICronquist[] = [{ id: 123 }];
        expectedResult = service.addCronquistToCollectionIfMissing(cronquistCollection, ...cronquistArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cronquist: ICronquist = { id: 123 };
        const cronquist2: ICronquist = { id: 456 };
        expectedResult = service.addCronquistToCollectionIfMissing([], cronquist, cronquist2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cronquist);
        expect(expectedResult).toContain(cronquist2);
      });

      it('should accept null and undefined values', () => {
        const cronquist: ICronquist = { id: 123 };
        expectedResult = service.addCronquistToCollectionIfMissing([], null, cronquist, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cronquist);
      });

      it('should return initial array if no Cronquist is added', () => {
        const cronquistCollection: ICronquist[] = [{ id: 123 }];
        expectedResult = service.addCronquistToCollectionIfMissing(cronquistCollection, undefined, null);
        expect(expectedResult).toEqual(cronquistCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
