jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAPGIV, APGIV } from '../apgiv.model';
import { APGIVService } from '../service/apgiv.service';

import { APGIVRoutingResolveService } from './apgiv-routing-resolve.service';

describe('APGIV routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: APGIVRoutingResolveService;
  let service: APGIVService;
  let resultAPGIV: IAPGIV | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(APGIVRoutingResolveService);
    service = TestBed.inject(APGIVService);
    resultAPGIV = undefined;
  });

  describe('resolve', () => {
    it('should return IAPGIV returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAPGIV = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAPGIV).toEqual({ id: 123 });
    });

    it('should return new IAPGIV if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAPGIV = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAPGIV).toEqual(new APGIV());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as APGIV })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAPGIV = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAPGIV).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
