import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClassificationNom, ClassificationNom } from '../classification-nom.model';
import { ClassificationNomService } from '../service/classification-nom.service';

@Injectable({ providedIn: 'root' })
export class ClassificationNomRoutingResolveService implements Resolve<IClassificationNom> {
  constructor(protected service: ClassificationNomService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClassificationNom> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((classificationNom: HttpResponse<ClassificationNom>) => {
          if (classificationNom.body) {
            return of(classificationNom.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ClassificationNom());
  }
}
