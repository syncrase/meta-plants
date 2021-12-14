import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRameau, Rameau } from '../rameau.model';
import { RameauService } from '../service/rameau.service';

@Injectable({ providedIn: 'root' })
export class RameauRoutingResolveService implements Resolve<IRameau> {
  constructor(protected service: RameauService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRameau> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rameau: HttpResponse<Rameau>) => {
          if (rameau.body) {
            return of(rameau.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Rameau());
  }
}
