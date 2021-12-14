import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IForme, Forme } from '../forme.model';
import { FormeService } from '../service/forme.service';

@Injectable({ providedIn: 'root' })
export class FormeRoutingResolveService implements Resolve<IForme> {
  constructor(protected service: FormeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IForme> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((forme: HttpResponse<Forme>) => {
          if (forme.body) {
            return of(forme.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Forme());
  }
}
