import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISuperOrdre, SuperOrdre } from '../super-ordre.model';
import { SuperOrdreService } from '../service/super-ordre.service';

@Injectable({ providedIn: 'root' })
export class SuperOrdreRoutingResolveService implements Resolve<ISuperOrdre> {
  constructor(protected service: SuperOrdreService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISuperOrdre> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((superOrdre: HttpResponse<SuperOrdre>) => {
          if (superOrdre.body) {
            return of(superOrdre.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SuperOrdre());
  }
}
