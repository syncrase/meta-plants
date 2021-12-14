import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEspece, Espece } from '../espece.model';
import { EspeceService } from '../service/espece.service';

@Injectable({ providedIn: 'root' })
export class EspeceRoutingResolveService implements Resolve<IEspece> {
  constructor(protected service: EspeceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEspece> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((espece: HttpResponse<Espece>) => {
          if (espece.body) {
            return of(espece.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Espece());
  }
}
