import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAPGIVPlante, APGIVPlante } from '../apgiv-plante.model';
import { APGIVPlanteService } from '../service/apgiv-plante.service';

@Injectable({ providedIn: 'root' })
export class APGIVPlanteRoutingResolveService implements Resolve<IAPGIVPlante> {
  constructor(protected service: APGIVPlanteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAPGIVPlante> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aPGIVPlante: HttpResponse<APGIVPlante>) => {
          if (aPGIVPlante.body) {
            return of(aPGIVPlante.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new APGIVPlante());
  }
}
