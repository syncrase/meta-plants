jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISousForme, SousForme } from '../sous-forme.model';
import { SousFormeService } from '../service/sous-forme.service';

import { SousFormeRoutingResolveService } from './sous-forme-routing-resolve.service';

describe('SousForme routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SousFormeRoutingResolveService;
  let service: SousFormeService;
  let resultSousForme: ISousForme | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SousFormeRoutingResolveService);
    service = TestBed.inject(SousFormeService);
    resultSousForme = undefined;
  });

  describe('resolve', () => {
    it('should return ISousForme returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousForme = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousForme).toEqual({ id: 123 });
    });

    it('should return new ISousForme if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousForme = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSousForme).toEqual(new SousForme());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SousForme })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousForme = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousForme).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
