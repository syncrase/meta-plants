jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISousDivision, SousDivision } from '../sous-division.model';
import { SousDivisionService } from '../service/sous-division.service';

import { SousDivisionRoutingResolveService } from './sous-division-routing-resolve.service';

describe('SousDivision routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SousDivisionRoutingResolveService;
  let service: SousDivisionService;
  let resultSousDivision: ISousDivision | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SousDivisionRoutingResolveService);
    service = TestBed.inject(SousDivisionService);
    resultSousDivision = undefined;
  });

  describe('resolve', () => {
    it('should return ISousDivision returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousDivision = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousDivision).toEqual({ id: 123 });
    });

    it('should return new ISousDivision if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousDivision = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSousDivision).toEqual(new SousDivision());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SousDivision })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousDivision = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousDivision).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
