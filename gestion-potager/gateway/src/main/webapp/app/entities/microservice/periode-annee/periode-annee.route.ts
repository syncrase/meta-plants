import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPeriodeAnnee, PeriodeAnnee } from 'app/shared/model/microservice/periode-annee.model';
import { PeriodeAnneeService } from './periode-annee.service';
import { PeriodeAnneeComponent } from './periode-annee.component';
import { PeriodeAnneeDetailComponent } from './periode-annee-detail.component';
import { PeriodeAnneeUpdateComponent } from './periode-annee-update.component';

@Injectable({ providedIn: 'root' })
export class PeriodeAnneeResolve implements Resolve<IPeriodeAnnee> {
  constructor(private service: PeriodeAnneeService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPeriodeAnnee> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((periodeAnnee: HttpResponse<PeriodeAnnee>) => {
          if (periodeAnnee.body) {
            return of(periodeAnnee.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PeriodeAnnee());
  }
}

export const periodeAnneeRoute: Routes = [
  {
    path: '',
    component: PeriodeAnneeComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gatewayApp.microservicePeriodeAnnee.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PeriodeAnneeDetailComponent,
    resolve: {
      periodeAnnee: PeriodeAnneeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microservicePeriodeAnnee.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PeriodeAnneeUpdateComponent,
    resolve: {
      periodeAnnee: PeriodeAnneeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microservicePeriodeAnnee.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PeriodeAnneeUpdateComponent,
    resolve: {
      periodeAnnee: PeriodeAnneeResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microservicePeriodeAnnee.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
