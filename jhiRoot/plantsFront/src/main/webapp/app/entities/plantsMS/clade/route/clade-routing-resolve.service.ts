import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClade, Clade } from '../clade.model';
import { CladeService } from '../service/clade.service';

@Injectable({ providedIn: 'root' })
export class CladeRoutingResolveService implements Resolve<IClade> {
  constructor(protected service: CladeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClade> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((clade: HttpResponse<Clade>) => {
          if (clade.body) {
            return of(clade.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Clade());
  }
}
