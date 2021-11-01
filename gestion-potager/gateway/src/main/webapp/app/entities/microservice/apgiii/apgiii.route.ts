import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAPGIII, APGIII } from 'app/shared/model/microservice/apgiii.model';
import { APGIIIService } from './apgiii.service';
import { APGIIIComponent } from './apgiii.component';
import { APGIIIDetailComponent } from './apgiii-detail.component';
import { APGIIIUpdateComponent } from './apgiii-update.component';

@Injectable({ providedIn: 'root' })
export class APGIIIResolve implements Resolve<IAPGIII> {
  constructor(private service: APGIIIService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAPGIII> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((aPGIII: HttpResponse<APGIII>) => {
          if (aPGIII.body) {
            return of(aPGIII.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new APGIII());
  }
}

export const aPGIIIRoute: Routes = [
  {
    path: '',
    component: APGIIIComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gatewayApp.microserviceAPgiii.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: APGIIIDetailComponent,
    resolve: {
      aPGIII: APGIIIResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceAPgiii.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: APGIIIUpdateComponent,
    resolve: {
      aPGIII: APGIIIResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceAPgiii.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: APGIIIUpdateComponent,
    resolve: {
      aPGIII: APGIIIResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceAPgiii.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
