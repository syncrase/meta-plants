import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAPGIV, APGIV } from 'app/shared/model/microservice/apgiv.model';
import { APGIVService } from './apgiv.service';
import { APGIVComponent } from './apgiv.component';
import { APGIVDetailComponent } from './apgiv-detail.component';
import { APGIVUpdateComponent } from './apgiv-update.component';

@Injectable({ providedIn: 'root' })
export class APGIVResolve implements Resolve<IAPGIV> {
  constructor(private service: APGIVService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAPGIV> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((aPGIV: HttpResponse<APGIV>) => {
          if (aPGIV.body) {
            return of(aPGIV.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new APGIV());
  }
}

export const aPGIVRoute: Routes = [
  {
    path: '',
    component: APGIVComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gatewayApp.microserviceAPgiv.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: APGIVDetailComponent,
    resolve: {
      aPGIV: APGIVResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceAPgiv.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: APGIVUpdateComponent,
    resolve: {
      aPGIV: APGIVResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceAPgiv.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: APGIVUpdateComponent,
    resolve: {
      aPGIV: APGIVResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceAPgiv.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
