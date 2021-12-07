jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRaunkier, Raunkier } from '../raunkier.model';
import { RaunkierService } from '../service/raunkier.service';

import { RaunkierRoutingResolveService } from './raunkier-routing-resolve.service';

describe('Raunkier routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RaunkierRoutingResolveService;
  let service: RaunkierService;
  let resultRaunkier: IRaunkier | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RaunkierRoutingResolveService);
    service = TestBed.inject(RaunkierService);
    resultRaunkier = undefined;
  });

  describe('resolve', () => {
    it('should return IRaunkier returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRaunkier = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRaunkier).toEqual({ id: 123 });
    });

    it('should return new IRaunkier if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRaunkier = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRaunkier).toEqual(new Raunkier());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Raunkier })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRaunkier = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRaunkier).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
