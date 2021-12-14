import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAPGIPlante, APGIPlante } from '../apgi-plante.model';
import { APGIPlanteService } from '../service/apgi-plante.service';

@Injectable({ providedIn: 'root' })
export class APGIPlanteRoutingResolveService implements Resolve<IAPGIPlante> {
  constructor(protected service: APGIPlanteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAPGIPlante> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((aPGIPlante: HttpResponse<APGIPlante>) => {
          if (aPGIPlante.body) {
            return of(aPGIPlante.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new APGIPlante());
  }
}
