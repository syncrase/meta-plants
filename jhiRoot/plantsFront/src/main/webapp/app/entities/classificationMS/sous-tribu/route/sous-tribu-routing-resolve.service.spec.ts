jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISousTribu, SousTribu } from '../sous-tribu.model';
import { SousTribuService } from '../service/sous-tribu.service';

import { SousTribuRoutingResolveService } from './sous-tribu-routing-resolve.service';

describe('SousTribu routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SousTribuRoutingResolveService;
  let service: SousTribuService;
  let resultSousTribu: ISousTribu | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SousTribuRoutingResolveService);
    service = TestBed.inject(SousTribuService);
    resultSousTribu = undefined;
  });

  describe('resolve', () => {
    it('should return ISousTribu returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousTribu = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousTribu).toEqual({ id: 123 });
    });

    it('should return new ISousTribu if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousTribu = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSousTribu).toEqual(new SousTribu());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SousTribu })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousTribu = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousTribu).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
