jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAPGII, APGII } from '../apgii.model';
import { APGIIService } from '../service/apgii.service';

import { APGIIRoutingResolveService } from './apgii-routing-resolve.service';

describe('APGII routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: APGIIRoutingResolveService;
  let service: APGIIService;
  let resultAPGII: IAPGII | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(APGIIRoutingResolveService);
    service = TestBed.inject(APGIIService);
    resultAPGII = undefined;
  });

  describe('resolve', () => {
    it('should return IAPGII returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAPGII = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAPGII).toEqual({ id: 123 });
    });

    it('should return new IAPGII if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAPGII = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAPGII).toEqual(new APGII());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as APGII })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAPGII = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAPGII).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
