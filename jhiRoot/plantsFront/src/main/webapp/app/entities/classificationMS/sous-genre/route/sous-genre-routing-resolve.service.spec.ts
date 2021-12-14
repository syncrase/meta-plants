jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISousGenre, SousGenre } from '../sous-genre.model';
import { SousGenreService } from '../service/sous-genre.service';

import { SousGenreRoutingResolveService } from './sous-genre-routing-resolve.service';

describe('SousGenre routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: SousGenreRoutingResolveService;
  let service: SousGenreService;
  let resultSousGenre: ISousGenre | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(SousGenreRoutingResolveService);
    service = TestBed.inject(SousGenreService);
    resultSousGenre = undefined;
  });

  describe('resolve', () => {
    it('should return ISousGenre returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousGenre = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousGenre).toEqual({ id: 123 });
    });

    it('should return new ISousGenre if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousGenre = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSousGenre).toEqual(new SousGenre());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as SousGenre })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultSousGenre = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultSousGenre).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
