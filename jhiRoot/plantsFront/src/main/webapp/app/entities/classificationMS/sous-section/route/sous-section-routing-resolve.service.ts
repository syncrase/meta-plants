import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISousSection, SousSection } from '../sous-section.model';
import { SousSectionService } from '../service/sous-section.service';

@Injectable({ providedIn: 'root' })
export class SousSectionRoutingResolveService implements Resolve<ISousSection> {
  constructor(protected service: SousSectionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISousSection> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sousSection: HttpResponse<SousSection>) => {
          if (sousSection.body) {
            return of(sousSection.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SousSection());
  }
}
