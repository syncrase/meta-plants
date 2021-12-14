jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInfraEmbranchement, InfraEmbranchement } from '../infra-embranchement.model';
import { InfraEmbranchementService } from '../service/infra-embranchement.service';

import { InfraEmbranchementRoutingResolveService } from './infra-embranchement-routing-resolve.service';

describe('InfraEmbranchement routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: InfraEmbranchementRoutingResolveService;
  let service: InfraEmbranchementService;
  let resultInfraEmbranchement: IInfraEmbranchement | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(InfraEmbranchementRoutingResolveService);
    service = TestBed.inject(InfraEmbranchementService);
    resultInfraEmbranchement = undefined;
  });

  describe('resolve', () => {
    it('should return IInfraEmbranchement returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInfraEmbranchement = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInfraEmbranchement).toEqual({ id: 123 });
    });

    it('should return new IInfraEmbranchement if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInfraEmbranchement = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInfraEmbranchement).toEqual(new InfraEmbranchement());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as InfraEmbranchement })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInfraEmbranchement = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInfraEmbranchement).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
