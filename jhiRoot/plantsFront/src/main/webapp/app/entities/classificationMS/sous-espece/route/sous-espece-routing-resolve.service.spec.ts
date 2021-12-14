jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISousEspece, SousEspece } from '../sous-espece.model';
import { SousEspeceService } from '../service/sous-espece.service';

import { SousEspeceRoutingResolveService } from './sous-espece-routing-resolve.service';

describe('SousEspece routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SousEspeceRoutingResolveService;
  let service: SousEspeceService;
  let resultSousEspece: ISousEspece | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SousEspeceRoutingResolveService);
    service = TestBed.inject(SousEspeceService);
    resultSousEspece = undefined;
  });

  describe('resolve', () => {
    it('should return ISousEspece returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousEspece = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousEspece).toEqual({ id: 123 });
    });

    it('should return new ISousEspece if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousEspece = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSousEspece).toEqual(new SousEspece());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SousEspece })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousEspece = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousEspece).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
