import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISousRegne, SousRegne } from '../sous-regne.model';
import { SousRegneService } from '../service/sous-regne.service';

@Injectable({ providedIn: 'root' })
export class SousRegneRoutingResolveService implements Resolve<ISousRegne> {
  constructor(protected service: SousRegneService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISousRegne> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sousRegne: HttpResponse<SousRegne>) => {
          if (sousRegne.body) {
            return of(sousRegne.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SousRegne());
  }
}
