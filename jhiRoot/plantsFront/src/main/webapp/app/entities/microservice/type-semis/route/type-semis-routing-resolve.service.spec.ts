jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITypeSemis, TypeSemis } from '../type-semis.model';
import { TypeSemisService } from '../service/type-semis.service';

import { TypeSemisRoutingResolveService } from './type-semis-routing-resolve.service';

describe('TypeSemis routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TypeSemisRoutingResolveService;
  let service: TypeSemisService;
  let resultTypeSemis: ITypeSemis | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TypeSemisRoutingResolveService);
    service = TestBed.inject(TypeSemisService);
    resultTypeSemis = undefined;
  });

  describe('resolve', () => {
    it('should return ITypeSemis returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTypeSemis = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTypeSemis).toEqual({ id: 123 });
    });

    it('should return new ITypeSemis if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTypeSemis = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTypeSemis).toEqual(new TypeSemis());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TypeSemis })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTypeSemis = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTypeSemis).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
