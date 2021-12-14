jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInfraOrdre, InfraOrdre } from '../infra-ordre.model';
import { InfraOrdreService } from '../service/infra-ordre.service';

import { InfraOrdreRoutingResolveService } from './infra-ordre-routing-resolve.service';

describe('InfraOrdre routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: InfraOrdreRoutingResolveService;
  let service: InfraOrdreService;
  let resultInfraOrdre: IInfraOrdre | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(InfraOrdreRoutingResolveService);
    service = TestBed.inject(InfraOrdreService);
    resultInfraOrdre = undefined;
  });

  describe('resolve', () => {
    it('should return IInfraOrdre returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInfraOrdre = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInfraOrdre).toEqual({ id: 123 });
    });

    it('should return new IInfraOrdre if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInfraOrdre = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInfraOrdre).toEqual(new InfraOrdre());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as InfraOrdre })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInfraOrdre = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInfraOrdre).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
