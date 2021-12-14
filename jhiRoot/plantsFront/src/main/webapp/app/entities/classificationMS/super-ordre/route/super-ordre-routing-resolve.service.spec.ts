jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISuperOrdre, SuperOrdre } from '../super-ordre.model';
import { SuperOrdreService } from '../service/super-ordre.service';

import { SuperOrdreRoutingResolveService } from './super-ordre-routing-resolve.service';

describe('SuperOrdre routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SuperOrdreRoutingResolveService;
  let service: SuperOrdreService;
  let resultSuperOrdre: ISuperOrdre | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SuperOrdreRoutingResolveService);
    service = TestBed.inject(SuperOrdreService);
    resultSuperOrdre = undefined;
  });

  describe('resolve', () => {
    it('should return ISuperOrdre returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSuperOrdre = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSuperOrdre).toEqual({ id: 123 });
    });

    it('should return new ISuperOrdre if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSuperOrdre = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSuperOrdre).toEqual(new SuperOrdre());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SuperOrdre })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSuperOrdre = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSuperOrdre).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
