jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEnsoleillement, Ensoleillement } from '../ensoleillement.model';
import { EnsoleillementService } from '../service/ensoleillement.service';

import { EnsoleillementRoutingResolveService } from './ensoleillement-routing-resolve.service';

describe('Ensoleillement routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: EnsoleillementRoutingResolveService;
  let service: EnsoleillementService;
  let resultEnsoleillement: IEnsoleillement | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(EnsoleillementRoutingResolveService);
    service = TestBed.inject(EnsoleillementService);
    resultEnsoleillement = undefined;
  });

  describe('resolve', () => {
    it('should return IEnsoleillement returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEnsoleillement = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEnsoleillement).toEqual({ id: 123 });
    });

    it('should return new IEnsoleillement if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEnsoleillement = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultEnsoleillement).toEqual(new Ensoleillement());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Ensoleillement })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultEnsoleillement = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultEnsoleillement).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
