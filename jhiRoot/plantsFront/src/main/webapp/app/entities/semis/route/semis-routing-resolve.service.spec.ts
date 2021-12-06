jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISemis, Semis } from '../semis.model';
import { SemisService } from '../service/semis.service';

import { SemisRoutingResolveService } from './semis-routing-resolve.service';

describe('Semis routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SemisRoutingResolveService;
  let service: SemisService;
  let resultSemis: ISemis | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SemisRoutingResolveService);
    service = TestBed.inject(SemisService);
    resultSemis = undefined;
  });

  describe('resolve', () => {
    it('should return ISemis returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSemis = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSemis).toEqual({ id: 123 });
    });

    it('should return new ISemis if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSemis = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSemis).toEqual(new Semis());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Semis })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSemis = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSemis).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
