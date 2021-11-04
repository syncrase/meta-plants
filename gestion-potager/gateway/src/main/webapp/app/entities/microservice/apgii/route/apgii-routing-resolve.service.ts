import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAPGII, APGII } from '../apgii.model';
import { APGIIService } from '../service/apgii.service';

@Injectable({ providedIn: 'root' })
export class APGIIRoutingResolveService implements Resolve<IAPGII> {
  constructor(protected service: APGIIService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAPGII> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aPGII: HttpResponse<APGII>) => {
          if (aPGII.body) {
            return of(aPGII.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new APGII());
  }
}
