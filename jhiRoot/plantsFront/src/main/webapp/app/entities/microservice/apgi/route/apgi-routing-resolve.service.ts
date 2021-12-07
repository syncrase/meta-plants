import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAPGI, APGI } from '../apgi.model';
import { APGIService } from '../service/apgi.service';

@Injectable({ providedIn: 'root' })
export class APGIRoutingResolveService implements Resolve<IAPGI> {
  constructor(protected service: APGIService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAPGI> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aPGI: HttpResponse<APGI>) => {
          if (aPGI.body) {
            return of(aPGI.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new APGI());
  }
}
