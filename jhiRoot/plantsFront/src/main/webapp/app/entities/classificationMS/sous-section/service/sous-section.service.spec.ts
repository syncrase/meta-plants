import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISousSection, SousSection } from '../sous-section.model';

import { SousSectionService } from './sous-section.service';

describe('SousSection Service', () => {
  let service: SousSectionService;
  let httpMock: HttpTestingController;
  let elemDefault: ISousSection;
  let expectedResult: ISousSection | ISousSection[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SousSectionService);
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

    it('should create a SousSection', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SousSection()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SousSection', () => {
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

    it('should partial update a SousSection', () => {
      const patchObject = Object.assign(
        {
          nomFr: 'BBBBBB',
        },
        new SousSection()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SousSection', () => {
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

    it('should delete a SousSection', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSousSectionToCollectionIfMissing', () => {
      it('should add a SousSection to an empty array', () => {
        const sousSection: ISousSection = { id: 123 };
        expectedResult = service.addSousSectionToCollectionIfMissing([], sousSection);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousSection);
      });

      it('should not add a SousSection to an array that contains it', () => {
        const sousSection: ISousSection = { id: 123 };
        const sousSectionCollection: ISousSection[] = [
          {
            ...sousSection,
          },
          { id: 456 },
        ];
        expectedResult = service.addSousSectionToCollectionIfMissing(sousSectionCollection, sousSection);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SousSection to an array that doesn't contain it", () => {
        const sousSection: ISousSection = { id: 123 };
        const sousSectionCollection: ISousSection[] = [{ id: 456 }];
        expectedResult = service.addSousSectionToCollectionIfMissing(sousSectionCollection, sousSection);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousSection);
      });

      it('should add only unique SousSection to an array', () => {
        const sousSectionArray: ISousSection[] = [{ id: 123 }, { id: 456 }, { id: 79671 }];
        const sousSectionCollection: ISousSection[] = [{ id: 123 }];
        expectedResult = service.addSousSectionToCollectionIfMissing(sousSectionCollection, ...sousSectionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sousSection: ISousSection = { id: 123 };
        const sousSection2: ISousSection = { id: 456 };
        expectedResult = service.addSousSectionToCollectionIfMissing([], sousSection, sousSection2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousSection);
        expect(expectedResult).toContain(sousSection2);
      });

      it('should accept null and undefined values', () => {
        const sousSection: ISousSection = { id: 123 };
        expectedResult = service.addSousSectionToCollectionIfMissing([], null, sousSection, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousSection);
      });

      it('should return initial array if no SousSection is added', () => {
        const sousSectionCollection: ISousSection[] = [{ id: 123 }];
        expectedResult = service.addSousSectionToCollectionIfMissing(sousSectionCollection, undefined, null);
        expect(expectedResult).toEqual(sousSectionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
