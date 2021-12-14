import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInfraEmbranchement, InfraEmbranchement } from '../infra-embranchement.model';
import { InfraEmbranchementService } from '../service/infra-embranchement.service';

@Injectable({ providedIn: 'root' })
export class InfraEmbranchementRoutingResolveService implements Resolve<IInfraEmbranchement> {
  constructor(protected service: InfraEmbranchementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInfraEmbranchement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((infraEmbranchement: HttpResponse<InfraEmbranchement>) => {
          if (infraEmbranchement.body) {
            return of(infraEmbranchement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InfraEmbranchement());
  }
}
