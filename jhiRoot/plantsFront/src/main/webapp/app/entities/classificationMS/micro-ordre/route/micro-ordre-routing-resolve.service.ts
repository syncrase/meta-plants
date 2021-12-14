import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMicroOrdre, MicroOrdre } from '../micro-ordre.model';
import { MicroOrdreService } from '../service/micro-ordre.service';

@Injectable({ providedIn: 'root' })
export class MicroOrdreRoutingResolveService implements Resolve<IMicroOrdre> {
  constructor(protected service: MicroOrdreService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMicroOrdre> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((microOrdre: HttpResponse<MicroOrdre>) => {
          if (microOrdre.body) {
            return of(microOrdre.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MicroOrdre());
  }
}
