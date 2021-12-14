import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITribu, Tribu } from '../tribu.model';
import { TribuService } from '../service/tribu.service';

@Injectable({ providedIn: 'root' })
export class TribuRoutingResolveService implements Resolve<ITribu> {
  constructor(protected service: TribuService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITribu> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tribu: HttpResponse<Tribu>) => {
          if (tribu.body) {
            return of(tribu.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tribu());
  }
}
