import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrdre, Ordre } from '../ordre.model';
import { OrdreService } from '../service/ordre.service';

@Injectable({ providedIn: 'root' })
export class OrdreRoutingResolveService implements Resolve<IOrdre> {
  constructor(protected service: OrdreService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrdre> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ordre: HttpResponse<Ordre>) => {
          if (ordre.body) {
            return of(ordre.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ordre());
  }
}
