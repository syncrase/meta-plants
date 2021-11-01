import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICronquist, Cronquist } from 'app/shared/model/microservice/cronquist.model';
import { CronquistService } from './cronquist.service';
import { CronquistComponent } from './cronquist.component';
import { CronquistDetailComponent } from './cronquist-detail.component';
import { CronquistUpdateComponent } from './cronquist-update.component';

@Injectable({ providedIn: 'root' })
export class CronquistResolve implements Resolve<ICronquist> {
  constructor(private service: CronquistService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICronquist> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((cronquist: HttpResponse<Cronquist>) => {
          if (cronquist.body) {
            return of(cronquist.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cronquist());
  }
}

export const cronquistRoute: Routes = [
  {
    path: '',
    component: CronquistComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gatewayApp.microserviceCronquist.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CronquistDetailComponent,
    resolve: {
      cronquist: CronquistResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceCronquist.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CronquistUpdateComponent,
    resolve: {
      cronquist: CronquistResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceCronquist.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CronquistUpdateComponent,
    resolve: {
      cronquist: CronquistResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceCronquist.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
