import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMicroEmbranchement, MicroEmbranchement } from '../micro-embranchement.model';
import { MicroEmbranchementService } from '../service/micro-embranchement.service';

@Injectable({ providedIn: 'root' })
export class MicroEmbranchementRoutingResolveService implements Resolve<IMicroEmbranchement> {
  constructor(protected service: MicroEmbranchementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMicroEmbranchement> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((microEmbranchement: HttpResponse<MicroEmbranchement>) => {
          if (microEmbranchement.body) {
            return of(microEmbranchement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MicroEmbranchement());
  }
}
