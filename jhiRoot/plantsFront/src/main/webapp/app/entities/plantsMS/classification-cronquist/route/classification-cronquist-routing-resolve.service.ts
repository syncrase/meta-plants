import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClassificationCronquist, ClassificationCronquist } from '../classification-cronquist.model';
import { ClassificationCronquistService } from '../service/classification-cronquist.service';

@Injectable({ providedIn: 'root' })
export class ClassificationCronquistRoutingResolveService implements Resolve<IClassificationCronquist> {
  constructor(protected service: ClassificationCronquistService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClassificationCronquist> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((classificationCronquist: HttpResponse<ClassificationCronquist>) => {
          if (classificationCronquist.body) {
            return of(classificationCronquist.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ClassificationCronquist());
  }
}
