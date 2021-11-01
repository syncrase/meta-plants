import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRessemblance, Ressemblance } from 'app/shared/model/microservice/ressemblance.model';
import { RessemblanceService } from './ressemblance.service';
import { RessemblanceComponent } from './ressemblance.component';
import { RessemblanceDetailComponent } from './ressemblance-detail.component';
import { RessemblanceUpdateComponent } from './ressemblance-update.component';

@Injectable({ providedIn: 'root' })
export class RessemblanceResolve implements Resolve<IRessemblance> {
  constructor(private service: RessemblanceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRessemblance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((ressemblance: HttpResponse<Ressemblance>) => {
          if (ressemblance.body) {
            return of(ressemblance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ressemblance());
  }
}

export const ressemblanceRoute: Routes = [
  {
    path: '',
    component: RessemblanceComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gatewayApp.microserviceRessemblance.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RessemblanceDetailComponent,
    resolve: {
      ressemblance: RessemblanceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceRessemblance.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RessemblanceUpdateComponent,
    resolve: {
      ressemblance: RessemblanceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceRessemblance.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RessemblanceUpdateComponent,
    resolve: {
      ressemblance: RessemblanceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceRessemblance.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
