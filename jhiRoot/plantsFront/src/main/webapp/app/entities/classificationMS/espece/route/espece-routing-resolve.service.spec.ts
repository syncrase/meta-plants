jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEspece, Espece } from '../espece.model';
import { EspeceService } from '../service/espece.service';

import { EspeceRoutingResolveService } from './espece-routing-resolve.service';

describe('Espece routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EspeceRoutingResolveService;
  let service: EspeceService;
  let resultEspece: IEspece | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(EspeceRoutingResolveService);
    service = TestBed.inject(EspeceService);
    resultEspece = undefined;
  });

  describe('resolve', () => {
    it('should return IEspece returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEspece = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEspece).toEqual({ id: 123 });
    });

    it('should return new IEspece if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEspece = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEspece).toEqual(new Espece());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Espece })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEspece = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEspece).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
