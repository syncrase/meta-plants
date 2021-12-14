jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISousVariete, SousVariete } from '../sous-variete.model';
import { SousVarieteService } from '../service/sous-variete.service';

import { SousVarieteRoutingResolveService } from './sous-variete-routing-resolve.service';

describe('SousVariete routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SousVarieteRoutingResolveService;
  let service: SousVarieteService;
  let resultSousVariete: ISousVariete | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SousVarieteRoutingResolveService);
    service = TestBed.inject(SousVarieteService);
    resultSousVariete = undefined;
  });

  describe('resolve', () => {
    it('should return ISousVariete returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousVariete = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousVariete).toEqual({ id: 123 });
    });

    it('should return new ISousVariete if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousVariete = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSousVariete).toEqual(new SousVariete());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SousVariete })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousVariete = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousVariete).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
