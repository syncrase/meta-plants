import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISuperFamille, SuperFamille } from '../super-famille.model';
import { SuperFamilleService } from '../service/super-famille.service';

@Injectable({ providedIn: 'root' })
export class SuperFamilleRoutingResolveService implements Resolve<ISuperFamille> {
  constructor(protected service: SuperFamilleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISuperFamille> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((superFamille: HttpResponse<SuperFamille>) => {
          if (superFamille.body) {
            return of(superFamille.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SuperFamille());
  }
}
