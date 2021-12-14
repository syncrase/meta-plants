jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISuperClasse, SuperClasse } from '../super-classe.model';
import { SuperClasseService } from '../service/super-classe.service';

import { SuperClasseRoutingResolveService } from './super-classe-routing-resolve.service';

describe('SuperClasse routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SuperClasseRoutingResolveService;
  let service: SuperClasseService;
  let resultSuperClasse: ISuperClasse | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SuperClasseRoutingResolveService);
    service = TestBed.inject(SuperClasseService);
    resultSuperClasse = undefined;
  });

  describe('resolve', () => {
    it('should return ISuperClasse returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSuperClasse = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSuperClasse).toEqual({ id: 123 });
    });

    it('should return new ISuperClasse if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSuperClasse = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSuperClasse).toEqual(new SuperClasse());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SuperClasse })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSuperClasse = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSuperClasse).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
