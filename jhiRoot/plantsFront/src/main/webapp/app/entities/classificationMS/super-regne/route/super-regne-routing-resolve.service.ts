import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISuperRegne, SuperRegne } from '../super-regne.model';
import { SuperRegneService } from '../service/super-regne.service';

@Injectable({ providedIn: 'root' })
export class SuperRegneRoutingResolveService implements Resolve<ISuperRegne> {
  constructor(protected service: SuperRegneService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISuperRegne> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((superRegne: HttpResponse<SuperRegne>) => {
          if (superRegne.body) {
            return of(superRegne.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SuperRegne());
  }
}
