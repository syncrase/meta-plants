import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInfraRegne, InfraRegne } from '../infra-regne.model';
import { InfraRegneService } from '../service/infra-regne.service';

@Injectable({ providedIn: 'root' })
export class InfraRegneRoutingResolveService implements Resolve<IInfraRegne> {
  constructor(protected service: InfraRegneService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInfraRegne> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((infraRegne: HttpResponse<InfraRegne>) => {
          if (infraRegne.body) {
            return of(infraRegne.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InfraRegne());
  }
}
