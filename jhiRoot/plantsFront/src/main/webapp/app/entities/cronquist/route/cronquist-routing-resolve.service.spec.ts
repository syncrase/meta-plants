jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICronquist, Cronquist } from '../cronquist.model';
import { CronquistService } from '../service/cronquist.service';

import { CronquistRoutingResolveService } from './cronquist-routing-resolve.service';

describe('Cronquist routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CronquistRoutingResolveService;
  let service: CronquistService;
  let resultCronquist: ICronquist | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CronquistRoutingResolveService);
    service = TestBed.inject(CronquistService);
    resultCronquist = undefined;
  });

  describe('resolve', () => {
    it('should return ICronquist returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCronquist = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCronquist).toEqual({ id: 123 });
    });

    it('should return new ICronquist if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCronquist = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCronquist).toEqual(new Cronquist());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Cronquist })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCronquist = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCronquist).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
