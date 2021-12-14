jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVariete, Variete } from '../variete.model';
import { VarieteService } from '../service/variete.service';

import { VarieteRoutingResolveService } from './variete-routing-resolve.service';

describe('Variete routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: VarieteRoutingResolveService;
  let service: VarieteService;
  let resultVariete: IVariete | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(VarieteRoutingResolveService);
    service = TestBed.inject(VarieteService);
    resultVariete = undefined;
  });

  describe('resolve', () => {
    it('should return IVariete returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVariete = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVariete).toEqual({ id: 123 });
    });

    it('should return new IVariete if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVariete = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultVariete).toEqual(new Variete());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Variete })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVariete = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVariete).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
