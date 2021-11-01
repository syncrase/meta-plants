import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IAllelopathie, Allelopathie } from 'app/shared/model/microservice/allelopathie.model';
import { AllelopathieService } from './allelopathie.service';
import { AllelopathieComponent } from './allelopathie.component';
import { AllelopathieDetailComponent } from './allelopathie-detail.component';
import { AllelopathieUpdateComponent } from './allelopathie-update.component';

@Injectable({ providedIn: 'root' })
export class AllelopathieResolve implements Resolve<IAllelopathie> {
  constructor(private service: AllelopathieService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAllelopathie> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((allelopathie: HttpResponse<Allelopathie>) => {
          if (allelopathie.body) {
            return of(allelopathie.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Allelopathie());
  }
}

export const allelopathieRoute: Routes = [
  {
    path: '',
    component: AllelopathieComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gatewayApp.microserviceAllelopathie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AllelopathieDetailComponent,
    resolve: {
      allelopathie: AllelopathieResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceAllelopathie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AllelopathieUpdateComponent,
    resolve: {
      allelopathie: AllelopathieResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceAllelopathie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AllelopathieUpdateComponent,
    resolve: {
      allelopathie: AllelopathieResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceAllelopathie.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
