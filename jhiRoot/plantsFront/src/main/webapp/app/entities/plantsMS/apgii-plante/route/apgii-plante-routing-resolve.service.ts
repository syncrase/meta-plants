import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAPGIIPlante, APGIIPlante } from '../apgii-plante.model';
import { APGIIPlanteService } from '../service/apgii-plante.service';

@Injectable({ providedIn: 'root' })
export class APGIIPlanteRoutingResolveService implements Resolve<IAPGIIPlante> {
  constructor(protected service: APGIIPlanteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAPGIIPlante> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aPGIIPlante: HttpResponse<APGIIPlante>) => {
          if (aPGIIPlante.body) {
            return of(aPGIIPlante.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new APGIIPlante());
  }
}
