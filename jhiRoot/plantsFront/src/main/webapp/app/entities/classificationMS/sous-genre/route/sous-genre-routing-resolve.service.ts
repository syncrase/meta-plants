import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISousGenre, SousGenre } from '../sous-genre.model';
import { SousGenreService } from '../service/sous-genre.service';

@Injectable({ providedIn: 'root' })
export class SousGenreRoutingResolveService implements Resolve<ISousGenre> {
  constructor(protected service: SousGenreService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISousGenre> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sousGenre: HttpResponse<SousGenre>) => {
          if (sousGenre.body) {
            return of(sousGenre.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SousGenre());
  }
}
