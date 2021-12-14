jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISousSection, SousSection } from '../sous-section.model';
import { SousSectionService } from '../service/sous-section.service';

import { SousSectionRoutingResolveService } from './sous-section-routing-resolve.service';

describe('SousSection routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SousSectionRoutingResolveService;
  let service: SousSectionService;
  let resultSousSection: ISousSection | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SousSectionRoutingResolveService);
    service = TestBed.inject(SousSectionService);
    resultSousSection = undefined;
  });

  describe('resolve', () => {
    it('should return ISousSection returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousSection = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousSection).toEqual({ id: 123 });
    });

    it('should return new ISousSection if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousSection = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSousSection).toEqual(new SousSection());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SousSection })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousSection = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousSection).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
