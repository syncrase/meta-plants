import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAPGIII, APGIII } from '../apgiii.model';
import { APGIIIService } from '../service/apgiii.service';

@Injectable({ providedIn: 'root' })
export class APGIIIRoutingResolveService implements Resolve<IAPGIII> {
  constructor(protected service: APGIIIService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAPGIII> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aPGIII: HttpResponse<APGIII>) => {
          if (aPGIII.body) {
            return of(aPGIII.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new APGIII());
  }
}
