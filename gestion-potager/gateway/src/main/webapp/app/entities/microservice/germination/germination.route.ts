import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IGermination, Germination } from 'app/shared/model/microservice/germination.model';
import { GerminationService } from './germination.service';
import { GerminationComponent } from './germination.component';
import { GerminationDetailComponent } from './germination-detail.component';
import { GerminationUpdateComponent } from './germination-update.component';

@Injectable({ providedIn: 'root' })
export class GerminationResolve implements Resolve<IGermination> {
  constructor(private service: GerminationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGermination> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((germination: HttpResponse<Germination>) => {
          if (germination.body) {
            return of(germination.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Germination());
  }
}

export const germinationRoute: Routes = [
  {
    path: '',
    component: GerminationComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gatewayApp.microserviceGermination.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GerminationDetailComponent,
    resolve: {
      germination: GerminationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceGermination.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GerminationUpdateComponent,
    resolve: {
      germination: GerminationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceGermination.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GerminationUpdateComponent,
    resolve: {
      germination: GerminationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceGermination.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
