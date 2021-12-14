jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInfraClasse, InfraClasse } from '../infra-classe.model';
import { InfraClasseService } from '../service/infra-classe.service';

import { InfraClasseRoutingResolveService } from './infra-classe-routing-resolve.service';

describe('InfraClasse routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: InfraClasseRoutingResolveService;
  let service: InfraClasseService;
  let resultInfraClasse: IInfraClasse | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(InfraClasseRoutingResolveService);
    service = TestBed.inject(InfraClasseService);
    resultInfraClasse = undefined;
  });

  describe('resolve', () => {
    it('should return IInfraClasse returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInfraClasse = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInfraClasse).toEqual({ id: 123 });
    });

    it('should return new IInfraClasse if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInfraClasse = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInfraClasse).toEqual(new InfraClasse());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as InfraClasse })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultInfraClasse = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInfraClasse).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
