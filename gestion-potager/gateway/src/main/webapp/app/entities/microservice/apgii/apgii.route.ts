import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAPGII, APGII } from 'app/shared/model/microservice/apgii.model';
import { APGIIService } from './apgii.service';
import { APGIIComponent } from './apgii.component';
import { APGIIDetailComponent } from './apgii-detail.component';
import { APGIIUpdateComponent } from './apgii-update.component';

@Injectable({ providedIn: 'root' })
export class APGIIResolve implements Resolve<IAPGII> {
  constructor(private service: APGIIService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAPGII> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((aPGII: HttpResponse<APGII>) => {
          if (aPGII.body) {
            return of(aPGII.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new APGII());
  }
}

export const aPGIIRoute: Routes = [
  {
    path: '',
    component: APGIIComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gatewayApp.microserviceAPgii.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: APGIIDetailComponent,
    resolve: {
      aPGII: APGIIResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceAPgii.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: APGIIUpdateComponent,
    resolve: {
      aPGII: APGIIResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceAPgii.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: APGIIUpdateComponent,
    resolve: {
      aPGII: APGIIResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceAPgii.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
