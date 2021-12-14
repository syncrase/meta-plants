jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAPGIVPlante, APGIVPlante } from '../apgiv-plante.model';
import { APGIVPlanteService } from '../service/apgiv-plante.service';

import { APGIVPlanteRoutingResolveService } from './apgiv-plante-routing-resolve.service';

describe('APGIVPlante routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: APGIVPlanteRoutingResolveService;
  let service: APGIVPlanteService;
  let resultAPGIVPlante: IAPGIVPlante | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(APGIVPlanteRoutingResolveService);
    service = TestBed.inject(APGIVPlanteService);
    resultAPGIVPlante = undefined;
  });

  describe('resolve', () => {
    it('should return IAPGIVPlante returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAPGIVPlante = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAPGIVPlante).toEqual({ id: 123 });
    });

    it('should return new IAPGIVPlante if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAPGIVPlante = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAPGIVPlante).toEqual(new APGIVPlante());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as APGIVPlante })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAPGIVPlante = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAPGIVPlante).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
