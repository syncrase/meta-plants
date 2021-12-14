import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISousClasse, SousClasse } from '../sous-classe.model';
import { SousClasseService } from '../service/sous-classe.service';

@Injectable({ providedIn: 'root' })
export class SousClasseRoutingResolveService implements Resolve<ISousClasse> {
  constructor(protected service: SousClasseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISousClasse> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sousClasse: HttpResponse<SousClasse>) => {
          if (sousClasse.body) {
            return of(sousClasse.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SousClasse());
  }
}
