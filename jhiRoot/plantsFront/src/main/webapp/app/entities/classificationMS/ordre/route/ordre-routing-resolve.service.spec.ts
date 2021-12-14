jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOrdre, Ordre } from '../ordre.model';
import { OrdreService } from '../service/ordre.service';

import { OrdreRoutingResolveService } from './ordre-routing-resolve.service';

describe('Ordre routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: OrdreRoutingResolveService;
  let service: OrdreService;
  let resultOrdre: IOrdre | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(OrdreRoutingResolveService);
    service = TestBed.inject(OrdreService);
    resultOrdre = undefined;
  });

  describe('resolve', () => {
    it('should return IOrdre returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrdre = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOrdre).toEqual({ id: 123 });
    });

    it('should return new IOrdre if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrdre = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultOrdre).toEqual(new Ordre());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Ordre })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultOrdre = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultOrdre).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
