import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExposition, Exposition } from '../exposition.model';
import { ExpositionService } from '../service/exposition.service';

@Injectable({ providedIn: 'root' })
export class ExpositionRoutingResolveService implements Resolve<IExposition> {
  constructor(protected service: ExpositionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExposition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((exposition: HttpResponse<Exposition>) => {
          if (exposition.body) {
            return of(exposition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Exposition());
  }
}
