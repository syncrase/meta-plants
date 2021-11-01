import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CronquistService } from 'app/entities/microservice/cronquist/cronquist.service';
import { ICronquist, Cronquist } from 'app/shared/model/microservice/cronquist.model';

describe('Service Tests', () => {
  describe('Cronquist Service', () => {
    let injector: TestBed;
    let service: CronquistService;
    let httpMock: HttpTestingController;
    let elemDefault: ICronquist;
    let expectedResult: ICronquist | ICronquist[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(CronquistService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Cronquist(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA');
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
            regne: 'BBBBBB',
            sousRegne: 'BBBBBB',
            division: 'BBBBBB',
            classe: 'BBBBBB',
            sousClasse: 'BBBBBB',
            ordre: 'BBBBBB',
            famille: 'BBBBBB',
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

      it('should return a list of Cronquist', () => {
        const returnedFromService = Object.assign(
          {
            regne: 'BBBBBB',
            sousRegne: 'BBBBBB',
            division: 'BBBBBB',
            classe: 'BBBBBB',
            sousClasse: 'BBBBBB',
            ordre: 'BBBBBB',
            famille: 'BBBBBB',
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

      it('should delete a Cronquist', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
