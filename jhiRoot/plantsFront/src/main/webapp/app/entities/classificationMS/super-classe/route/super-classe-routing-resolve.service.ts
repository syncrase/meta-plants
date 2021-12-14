import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISuperClasse, SuperClasse } from '../super-classe.model';
import { SuperClasseService } from '../service/super-classe.service';

@Injectable({ providedIn: 'root' })
export class SuperClasseRoutingResolveService implements Resolve<ISuperClasse> {
  constructor(protected service: SuperClasseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISuperClasse> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((superClasse: HttpResponse<SuperClasse>) => {
          if (superClasse.body) {
            return of(superClasse.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SuperClasse());
  }
}
