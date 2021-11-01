import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAPGI, APGI } from 'app/shared/model/microservice/apgi.model';
import { APGIService } from './apgi.service';
import { APGIComponent } from './apgi.component';
import { APGIDetailComponent } from './apgi-detail.component';
import { APGIUpdateComponent } from './apgi-update.component';

@Injectable({ providedIn: 'root' })
export class APGIResolve implements Resolve<IAPGI> {
  constructor(private service: APGIService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAPGI> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((aPGI: HttpResponse<APGI>) => {
          if (aPGI.body) {
            return of(aPGI.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new APGI());
  }
}

export const aPGIRoute: Routes = [
  {
    path: '',
    component: APGIComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gatewayApp.microserviceAPgi.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: APGIDetailComponent,
    resolve: {
      aPGI: APGIResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceAPgi.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: APGIUpdateComponent,
    resolve: {
      aPGI: APGIResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceAPgi.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: APGIUpdateComponent,
    resolve: {
      aPGI: APGIResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceAPgi.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
