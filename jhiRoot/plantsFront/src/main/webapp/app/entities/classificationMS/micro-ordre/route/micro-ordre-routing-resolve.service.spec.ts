jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMicroOrdre, MicroOrdre } from '../micro-ordre.model';
import { MicroOrdreService } from '../service/micro-ordre.service';

import { MicroOrdreRoutingResolveService } from './micro-ordre-routing-resolve.service';

describe('MicroOrdre routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MicroOrdreRoutingResolveService;
  let service: MicroOrdreService;
  let resultMicroOrdre: IMicroOrdre | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(MicroOrdreRoutingResolveService);
    service = TestBed.inject(MicroOrdreService);
    resultMicroOrdre = undefined;
  });

  describe('resolve', () => {
    it('should return IMicroOrdre returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMicroOrdre = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMicroOrdre).toEqual({ id: 123 });
    });

    it('should return new IMicroOrdre if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMicroOrdre = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMicroOrdre).toEqual(new MicroOrdre());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MicroOrdre })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMicroOrdre = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMicroOrdre).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
