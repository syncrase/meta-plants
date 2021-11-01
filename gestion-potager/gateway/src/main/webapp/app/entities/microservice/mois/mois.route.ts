import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMois, Mois } from 'app/shared/model/microservice/mois.model';
import { MoisService } from './mois.service';
import { MoisComponent } from './mois.component';
import { MoisDetailComponent } from './mois-detail.component';
import { MoisUpdateComponent } from './mois-update.component';

@Injectable({ providedIn: 'root' })
export class MoisResolve implements Resolve<IMois> {
  constructor(private service: MoisService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMois> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((mois: HttpResponse<Mois>) => {
          if (mois.body) {
            return of(mois.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Mois());
  }
}

export const moisRoute: Routes = [
  {
    path: '',
    component: MoisComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gatewayApp.microserviceMois.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MoisDetailComponent,
    resolve: {
      mois: MoisResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceMois.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MoisUpdateComponent,
    resolve: {
      mois: MoisResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceMois.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MoisUpdateComponent,
    resolve: {
      mois: MoisResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceMois.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
