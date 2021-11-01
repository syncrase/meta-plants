import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISol, Sol } from 'app/shared/model/microservice/sol.model';
import { SolService } from './sol.service';
import { SolComponent } from './sol.component';
import { SolDetailComponent } from './sol-detail.component';
import { SolUpdateComponent } from './sol-update.component';

@Injectable({ providedIn: 'root' })
export class SolResolve implements Resolve<ISol> {
  constructor(private service: SolService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISol> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((sol: HttpResponse<Sol>) => {
          if (sol.body) {
            return of(sol.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Sol());
  }
}

export const solRoute: Routes = [
  {
    path: '',
    component: SolComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gatewayApp.microserviceSol.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SolDetailComponent,
    resolve: {
      sol: SolResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceSol.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SolUpdateComponent,
    resolve: {
      sol: SolResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceSol.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SolUpdateComponent,
    resolve: {
      sol: SolResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceSol.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
