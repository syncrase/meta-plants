import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISuperDivision, SuperDivision } from '../super-division.model';
import { SuperDivisionService } from '../service/super-division.service';

@Injectable({ providedIn: 'root' })
export class SuperDivisionRoutingResolveService implements Resolve<ISuperDivision> {
  constructor(protected service: SuperDivisionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISuperDivision> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((superDivision: HttpResponse<SuperDivision>) => {
          if (superDivision.body) {
            return of(superDivision.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SuperDivision());
  }
}
