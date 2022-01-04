jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IClassificationNom, ClassificationNom } from '../classification-nom.model';
import { ClassificationNomService } from '../service/classification-nom.service';

import { ClassificationNomRoutingResolveService } from './classification-nom-routing-resolve.service';

describe('ClassificationNom routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ClassificationNomRoutingResolveService;
  let service: ClassificationNomService;
  let resultClassificationNom: IClassificationNom | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ClassificationNomRoutingResolveService);
    service = TestBed.inject(ClassificationNomService);
    resultClassificationNom = undefined;
  });

  describe('resolve', () => {
    it('should return IClassificationNom returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultClassificationNom = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultClassificationNom).toEqual({ id: 123 });
    });

    it('should return new IClassificationNom if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultClassificationNom = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultClassificationNom).toEqual(new ClassificationNom());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ClassificationNom })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultClassificationNom = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultClassificationNom).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
