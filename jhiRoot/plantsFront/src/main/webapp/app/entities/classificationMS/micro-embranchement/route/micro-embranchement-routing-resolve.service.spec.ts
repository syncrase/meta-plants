jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMicroEmbranchement, MicroEmbranchement } from '../micro-embranchement.model';
import { MicroEmbranchementService } from '../service/micro-embranchement.service';

import { MicroEmbranchementRoutingResolveService } from './micro-embranchement-routing-resolve.service';

describe('MicroEmbranchement routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: MicroEmbranchementRoutingResolveService;
  let service: MicroEmbranchementService;
  let resultMicroEmbranchement: IMicroEmbranchement | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(MicroEmbranchementRoutingResolveService);
    service = TestBed.inject(MicroEmbranchementService);
    resultMicroEmbranchement = undefined;
  });

  describe('resolve', () => {
    it('should return IMicroEmbranchement returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMicroEmbranchement = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMicroEmbranchement).toEqual({ id: 123 });
    });

    it('should return new IMicroEmbranchement if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMicroEmbranchement = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultMicroEmbranchement).toEqual(new MicroEmbranchement());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MicroEmbranchement })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultMicroEmbranchement = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultMicroEmbranchement).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
