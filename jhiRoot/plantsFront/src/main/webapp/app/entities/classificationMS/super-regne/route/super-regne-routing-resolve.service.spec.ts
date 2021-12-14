jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISuperRegne, SuperRegne } from '../super-regne.model';
import { SuperRegneService } from '../service/super-regne.service';

import { SuperRegneRoutingResolveService } from './super-regne-routing-resolve.service';

describe('SuperRegne routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SuperRegneRoutingResolveService;
  let service: SuperRegneService;
  let resultSuperRegne: ISuperRegne | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SuperRegneRoutingResolveService);
    service = TestBed.inject(SuperRegneService);
    resultSuperRegne = undefined;
  });

  describe('resolve', () => {
    it('should return ISuperRegne returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSuperRegne = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSuperRegne).toEqual({ id: 123 });
    });

    it('should return new ISuperRegne if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSuperRegne = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSuperRegne).toEqual(new SuperRegne());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SuperRegne })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSuperRegne = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSuperRegne).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
