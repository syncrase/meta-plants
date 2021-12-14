import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAPGIIIPlante, APGIIIPlante } from '../apgiii-plante.model';
import { APGIIIPlanteService } from '../service/apgiii-plante.service';

@Injectable({ providedIn: 'root' })
export class APGIIIPlanteRoutingResolveService implements Resolve<IAPGIIIPlante> {
  constructor(protected service: APGIIIPlanteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAPGIIIPlante> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aPGIIIPlante: HttpResponse<APGIIIPlante>) => {
          if (aPGIIIPlante.body) {
            return of(aPGIIIPlante.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new APGIIIPlante());
  }
}
