import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISousTribu, SousTribu } from '../sous-tribu.model';
import { SousTribuService } from '../service/sous-tribu.service';

@Injectable({ providedIn: 'root' })
export class SousTribuRoutingResolveService implements Resolve<ISousTribu> {
  constructor(protected service: SousTribuService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISousTribu> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sousTribu: HttpResponse<SousTribu>) => {
          if (sousTribu.body) {
            return of(sousTribu.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SousTribu());
  }
}
