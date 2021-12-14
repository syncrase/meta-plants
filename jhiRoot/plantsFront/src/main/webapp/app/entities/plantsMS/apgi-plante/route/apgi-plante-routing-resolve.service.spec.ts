jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAPGIPlante, APGIPlante } from '../apgi-plante.model';
import { APGIPlanteService } from '../service/apgi-plante.service';

import { APGIPlanteRoutingResolveService } from './apgi-plante-routing-resolve.service';

describe('APGIPlante routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: APGIPlanteRoutingResolveService;
  let service: APGIPlanteService;
  let resultAPGIPlante: IAPGIPlante | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(APGIPlanteRoutingResolveService);
    service = TestBed.inject(APGIPlanteService);
    resultAPGIPlante = undefined;
  });

  describe('resolve', () => {
    it('should return IAPGIPlante returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAPGIPlante = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAPGIPlante).toEqual({ id: 123 });
    });

    it('should return new IAPGIPlante if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAPGIPlante = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAPGIPlante).toEqual(new APGIPlante());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as APGIPlante })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAPGIPlante = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAPGIPlante).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
