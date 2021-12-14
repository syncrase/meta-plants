jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITribu, Tribu } from '../tribu.model';
import { TribuService } from '../service/tribu.service';

import { TribuRoutingResolveService } from './tribu-routing-resolve.service';

describe('Tribu routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TribuRoutingResolveService;
  let service: TribuService;
  let resultTribu: ITribu | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(TribuRoutingResolveService);
    service = TestBed.inject(TribuService);
    resultTribu = undefined;
  });

  describe('resolve', () => {
    it('should return ITribu returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTribu = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTribu).toEqual({ id: 123 });
    });

    it('should return new ITribu if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTribu = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTribu).toEqual(new Tribu());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Tribu })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTribu = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTribu).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
