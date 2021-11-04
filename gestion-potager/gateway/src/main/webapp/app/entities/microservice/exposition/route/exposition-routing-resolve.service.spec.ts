jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IExposition, Exposition } from '../exposition.model';
import { ExpositionService } from '../service/exposition.service';

import { ExpositionRoutingResolveService } from './exposition-routing-resolve.service';

describe('Exposition routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ExpositionRoutingResolveService;
  let service: ExpositionService;
  let resultExposition: IExposition | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ExpositionRoutingResolveService);
    service = TestBed.inject(ExpositionService);
    resultExposition = undefined;
  });

  describe('resolve', () => {
    it('should return IExposition returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultExposition = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultExposition).toEqual({ id: 123 });
    });

    it('should return new IExposition if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultExposition = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultExposition).toEqual(new Exposition());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Exposition })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultExposition = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultExposition).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
