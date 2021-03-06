jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IClassificationCronquist, ClassificationCronquist } from '../classification-cronquist.model';
import { ClassificationCronquistService } from '../service/classification-cronquist.service';

import { ClassificationCronquistRoutingResolveService } from './classification-cronquist-routing-resolve.service';

describe('ClassificationCronquist routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ClassificationCronquistRoutingResolveService;
  let service: ClassificationCronquistService;
  let resultClassificationCronquist: IClassificationCronquist | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ClassificationCronquistRoutingResolveService);
    service = TestBed.inject(ClassificationCronquistService);
    resultClassificationCronquist = undefined;
  });

  describe('resolve', () => {
    it('should return IClassificationCronquist returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultClassificationCronquist = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultClassificationCronquist).toEqual({ id: 123 });
    });

    it('should return new IClassificationCronquist if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultClassificationCronquist = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultClassificationCronquist).toEqual(new ClassificationCronquist());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ClassificationCronquist })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultClassificationCronquist = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultClassificationCronquist).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
