import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISemis, Semis } from 'app/shared/model/microservice/semis.model';
import { SemisService } from './semis.service';
import { SemisComponent } from './semis.component';
import { SemisDetailComponent } from './semis-detail.component';
import { SemisUpdateComponent } from './semis-update.component';

@Injectable({ providedIn: 'root' })
export class SemisResolve implements Resolve<ISemis> {
  constructor(private service: SemisService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISemis> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((semis: HttpResponse<Semis>) => {
          if (semis.body) {
            return of(semis.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Semis());
  }
}

export const semisRoute: Routes = [
  {
    path: '',
    component: SemisComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gatewayApp.microserviceSemis.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SemisDetailComponent,
    resolve: {
      semis: SemisResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceSemis.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SemisUpdateComponent,
    resolve: {
      semis: SemisResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceSemis.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SemisUpdateComponent,
    resolve: {
      semis: SemisResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceSemis.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
