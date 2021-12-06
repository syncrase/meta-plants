import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICronquist, Cronquist } from '../cronquist.model';
import { CronquistService } from '../service/cronquist.service';

@Injectable({ providedIn: 'root' })
export class CronquistRoutingResolveService implements Resolve<ICronquist> {
  constructor(protected service: CronquistService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICronquist> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cronquist: HttpResponse<Cronquist>) => {
          if (cronquist.body) {
            return of(cronquist.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cronquist());
  }
}
