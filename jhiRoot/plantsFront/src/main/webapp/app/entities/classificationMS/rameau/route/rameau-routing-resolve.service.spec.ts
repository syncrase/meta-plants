jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IRameau, Rameau } from '../rameau.model';
import { RameauService } from '../service/rameau.service';

import { RameauRoutingResolveService } from './rameau-routing-resolve.service';

describe('Rameau routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RameauRoutingResolveService;
  let service: RameauService;
  let resultRameau: IRameau | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(RameauRoutingResolveService);
    service = TestBed.inject(RameauService);
    resultRameau = undefined;
  });

  describe('resolve', () => {
    it('should return IRameau returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRameau = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRameau).toEqual({ id: 123 });
    });

    it('should return new IRameau if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRameau = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRameau).toEqual(new Rameau());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Rameau })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRameau = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRameau).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
