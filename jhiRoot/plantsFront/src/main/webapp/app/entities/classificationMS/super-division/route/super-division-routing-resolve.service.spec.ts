jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISuperDivision, SuperDivision } from '../super-division.model';
import { SuperDivisionService } from '../service/super-division.service';

import { SuperDivisionRoutingResolveService } from './super-division-routing-resolve.service';

describe('SuperDivision routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SuperDivisionRoutingResolveService;
  let service: SuperDivisionService;
  let resultSuperDivision: ISuperDivision | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SuperDivisionRoutingResolveService);
    service = TestBed.inject(SuperDivisionService);
    resultSuperDivision = undefined;
  });

  describe('resolve', () => {
    it('should return ISuperDivision returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSuperDivision = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSuperDivision).toEqual({ id: 123 });
    });

    it('should return new ISuperDivision if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSuperDivision = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSuperDivision).toEqual(new SuperDivision());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SuperDivision })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSuperDivision = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSuperDivision).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
