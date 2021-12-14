import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISousOrdre, SousOrdre } from '../sous-ordre.model';
import { SousOrdreService } from '../service/sous-ordre.service';

@Injectable({ providedIn: 'root' })
export class SousOrdreRoutingResolveService implements Resolve<ISousOrdre> {
  constructor(protected service: SousOrdreService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISousOrdre> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sousOrdre: HttpResponse<SousOrdre>) => {
          if (sousOrdre.body) {
            return of(sousOrdre.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SousOrdre());
  }
}
