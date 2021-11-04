jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IGermination, Germination } from '../germination.model';
import { GerminationService } from '../service/germination.service';

import { GerminationRoutingResolveService } from './germination-routing-resolve.service';

describe('Germination routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: GerminationRoutingResolveService;
  let service: GerminationService;
  let resultGermination: IGermination | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(GerminationRoutingResolveService);
    service = TestBed.inject(GerminationService);
    resultGermination = undefined;
  });

  describe('resolve', () => {
    it('should return IGermination returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGermination = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGermination).toEqual({ id: 123 });
    });

    it('should return new IGermination if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGermination = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultGermination).toEqual(new Germination());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Germination })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultGermination = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultGermination).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
