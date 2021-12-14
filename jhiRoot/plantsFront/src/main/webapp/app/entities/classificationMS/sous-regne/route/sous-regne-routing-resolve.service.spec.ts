jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISousRegne, SousRegne } from '../sous-regne.model';
import { SousRegneService } from '../service/sous-regne.service';

import { SousRegneRoutingResolveService } from './sous-regne-routing-resolve.service';

describe('SousRegne routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SousRegneRoutingResolveService;
  let service: SousRegneService;
  let resultSousRegne: ISousRegne | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SousRegneRoutingResolveService);
    service = TestBed.inject(SousRegneService);
    resultSousRegne = undefined;
  });

  describe('resolve', () => {
    it('should return ISousRegne returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousRegne = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousRegne).toEqual({ id: 123 });
    });

    it('should return new ISousRegne if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousRegne = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSousRegne).toEqual(new SousRegne());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SousRegne })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousRegne = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousRegne).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
