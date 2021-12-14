jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISousClasse, SousClasse } from '../sous-classe.model';
import { SousClasseService } from '../service/sous-classe.service';

import { SousClasseRoutingResolveService } from './sous-classe-routing-resolve.service';

describe('SousClasse routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SousClasseRoutingResolveService;
  let service: SousClasseService;
  let resultSousClasse: ISousClasse | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SousClasseRoutingResolveService);
    service = TestBed.inject(SousClasseService);
    resultSousClasse = undefined;
  });

  describe('resolve', () => {
    it('should return ISousClasse returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousClasse = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousClasse).toEqual({ id: 123 });
    });

    it('should return new ISousClasse if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousClasse = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSousClasse).toEqual(new SousClasse());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SousClasse })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousClasse = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousClasse).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
