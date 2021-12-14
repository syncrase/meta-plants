import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISousForme, SousForme } from '../sous-forme.model';
import { SousFormeService } from '../service/sous-forme.service';

@Injectable({ providedIn: 'root' })
export class SousFormeRoutingResolveService implements Resolve<ISousForme> {
  constructor(protected service: SousFormeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISousForme> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sousForme: HttpResponse<SousForme>) => {
          if (sousForme.body) {
            return of(sousForme.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SousForme());
  }
}
