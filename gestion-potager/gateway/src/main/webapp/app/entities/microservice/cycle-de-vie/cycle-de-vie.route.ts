import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICycleDeVie, CycleDeVie } from 'app/shared/model/microservice/cycle-de-vie.model';
import { CycleDeVieService } from './cycle-de-vie.service';
import { CycleDeVieComponent } from './cycle-de-vie.component';
import { CycleDeVieDetailComponent } from './cycle-de-vie-detail.component';
import { CycleDeVieUpdateComponent } from './cycle-de-vie-update.component';

@Injectable({ providedIn: 'root' })
export class CycleDeVieResolve implements Resolve<ICycleDeVie> {
  constructor(private service: CycleDeVieService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICycleDeVie> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((cycleDeVie: HttpResponse<CycleDeVie>) => {
          if (cycleDeVie.body) {
            return of(cycleDeVie.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CycleDeVie());
  }
}

export const cycleDeVieRoute: Routes = [
  {
    path: '',
    component: CycleDeVieComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gatewayApp.microserviceCycleDeVie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CycleDeVieDetailComponent,
    resolve: {
      cycleDeVie: CycleDeVieResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceCycleDeVie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CycleDeVieUpdateComponent,
    resolve: {
      cycleDeVie: CycleDeVieResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceCycleDeVie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CycleDeVieUpdateComponent,
    resolve: {
      cycleDeVie: CycleDeVieResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceCycleDeVie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
