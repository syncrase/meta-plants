import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInfraClasse, InfraClasse } from '../infra-classe.model';
import { InfraClasseService } from '../service/infra-classe.service';

@Injectable({ providedIn: 'root' })
export class InfraClasseRoutingResolveService implements Resolve<IInfraClasse> {
  constructor(protected service: InfraClasseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInfraClasse> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((infraClasse: HttpResponse<InfraClasse>) => {
          if (infraClasse.body) {
            return of(infraClasse.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InfraClasse());
  }
}
