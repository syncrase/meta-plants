import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAPGIV, APGIV } from '../apgiv.model';
import { APGIVService } from '../service/apgiv.service';

@Injectable({ providedIn: 'root' })
export class APGIVRoutingResolveService implements Resolve<IAPGIV> {
  constructor(protected service: APGIVService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAPGIV> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aPGIV: HttpResponse<APGIV>) => {
          if (aPGIV.body) {
            return of(aPGIV.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new APGIV());
  }
}
