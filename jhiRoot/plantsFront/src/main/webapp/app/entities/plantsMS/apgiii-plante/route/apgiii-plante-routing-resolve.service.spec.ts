jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAPGIIIPlante, APGIIIPlante } from '../apgiii-plante.model';
import { APGIIIPlanteService } from '../service/apgiii-plante.service';

import { APGIIIPlanteRoutingResolveService } from './apgiii-plante-routing-resolve.service';

describe('APGIIIPlante routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: APGIIIPlanteRoutingResolveService;
  let service: APGIIIPlanteService;
  let resultAPGIIIPlante: IAPGIIIPlante | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(APGIIIPlanteRoutingResolveService);
    service = TestBed.inject(APGIIIPlanteService);
    resultAPGIIIPlante = undefined;
  });

  describe('resolve', () => {
    it('should return IAPGIIIPlante returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAPGIIIPlante = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAPGIIIPlante).toEqual({ id: 123 });
    });

    it('should return new IAPGIIIPlante if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAPGIIIPlante = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAPGIIIPlante).toEqual(new APGIIIPlante());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as APGIIIPlante })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAPGIIIPlante = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAPGIIIPlante).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
