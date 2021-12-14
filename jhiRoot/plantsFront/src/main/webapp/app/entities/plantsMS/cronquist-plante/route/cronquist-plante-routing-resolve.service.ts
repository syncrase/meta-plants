import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICronquistPlante, CronquistPlante } from '../cronquist-plante.model';
import { CronquistPlanteService } from '../service/cronquist-plante.service';

@Injectable({ providedIn: 'root' })
export class CronquistPlanteRoutingResolveService implements Resolve<ICronquistPlante> {
  constructor(protected service: CronquistPlanteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICronquistPlante> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cronquistPlante: HttpResponse<CronquistPlante>) => {
          if (cronquistPlante.body) {
            return of(cronquistPlante.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CronquistPlante());
  }
}
