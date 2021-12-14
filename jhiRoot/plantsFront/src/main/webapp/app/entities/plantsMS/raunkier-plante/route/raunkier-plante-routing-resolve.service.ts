import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRaunkierPlante, RaunkierPlante } from '../raunkier-plante.model';
import { RaunkierPlanteService } from '../service/raunkier-plante.service';

@Injectable({ providedIn: 'root' })
export class RaunkierPlanteRoutingResolveService implements Resolve<IRaunkierPlante> {
  constructor(protected service: RaunkierPlanteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRaunkierPlante> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((raunkierPlante: HttpResponse<RaunkierPlante>) => {
          if (raunkierPlante.body) {
            return of(raunkierPlante.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RaunkierPlante());
  }
}
