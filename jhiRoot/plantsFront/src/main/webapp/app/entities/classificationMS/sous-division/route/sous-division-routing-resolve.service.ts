import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISousDivision, SousDivision } from '../sous-division.model';
import { SousDivisionService } from '../service/sous-division.service';

@Injectable({ providedIn: 'root' })
export class SousDivisionRoutingResolveService implements Resolve<ISousDivision> {
  constructor(protected service: SousDivisionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISousDivision> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sousDivision: HttpResponse<SousDivision>) => {
          if (sousDivision.body) {
            return of(sousDivision.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SousDivision());
  }
}
