import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRegne, Regne } from '../regne.model';
import { RegneService } from '../service/regne.service';

@Injectable({ providedIn: 'root' })
export class RegneRoutingResolveService implements Resolve<IRegne> {
  constructor(protected service: RegneService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRegne> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((regne: HttpResponse<Regne>) => {
          if (regne.body) {
            return of(regne.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Regne());
  }
}
