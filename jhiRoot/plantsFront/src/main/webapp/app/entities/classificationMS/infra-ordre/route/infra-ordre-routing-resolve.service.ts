import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInfraOrdre, InfraOrdre } from '../infra-ordre.model';
import { InfraOrdreService } from '../service/infra-ordre.service';

@Injectable({ providedIn: 'root' })
export class InfraOrdreRoutingResolveService implements Resolve<IInfraOrdre> {
  constructor(protected service: InfraOrdreService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInfraOrdre> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((infraOrdre: HttpResponse<InfraOrdre>) => {
          if (infraOrdre.body) {
            return of(infraOrdre.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InfraOrdre());
  }
}
