jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICronquistPlante, CronquistPlante } from '../cronquist-plante.model';
import { CronquistPlanteService } from '../service/cronquist-plante.service';

import { CronquistPlanteRoutingResolveService } from './cronquist-plante-routing-resolve.service';

describe('CronquistPlante routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CronquistPlanteRoutingResolveService;
  let service: CronquistPlanteService;
  let resultCronquistPlante: ICronquistPlante | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CronquistPlanteRoutingResolveService);
    service = TestBed.inject(CronquistPlanteService);
    resultCronquistPlante = undefined;
  });

  describe('resolve', () => {
    it('should return ICronquistPlante returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCronquistPlante = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCronquistPlante).toEqual({ id: 123 });
    });

    it('should return new ICronquistPlante if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCronquistPlante = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCronquistPlante).toEqual(new CronquistPlante());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CronquistPlante })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCronquistPlante = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCronquistPlante).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
