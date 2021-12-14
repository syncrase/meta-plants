import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISousEspece, SousEspece } from '../sous-espece.model';
import { SousEspeceService } from '../service/sous-espece.service';

@Injectable({ providedIn: 'root' })
export class SousEspeceRoutingResolveService implements Resolve<ISousEspece> {
  constructor(protected service: SousEspeceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISousEspece> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sousEspece: HttpResponse<SousEspece>) => {
          if (sousEspece.body) {
            return of(sousEspece.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SousEspece());
  }
}
