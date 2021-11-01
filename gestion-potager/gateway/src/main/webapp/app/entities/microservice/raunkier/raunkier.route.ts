import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IRaunkier, Raunkier } from 'app/shared/model/microservice/raunkier.model';
import { RaunkierService } from './raunkier.service';
import { RaunkierComponent } from './raunkier.component';
import { RaunkierDetailComponent } from './raunkier-detail.component';
import { RaunkierUpdateComponent } from './raunkier-update.component';

@Injectable({ providedIn: 'root' })
export class RaunkierResolve implements Resolve<IRaunkier> {
  constructor(private service: RaunkierService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRaunkier> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((raunkier: HttpResponse<Raunkier>) => {
          if (raunkier.body) {
            return of(raunkier.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Raunkier());
  }
}

export const raunkierRoute: Routes = [
  {
    path: '',
    component: RaunkierComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gatewayApp.microserviceRaunkier.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RaunkierDetailComponent,
    resolve: {
      raunkier: RaunkierResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceRaunkier.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RaunkierUpdateComponent,
    resolve: {
      raunkier: RaunkierResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceRaunkier.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RaunkierUpdateComponent,
    resolve: {
      raunkier: RaunkierResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceRaunkier.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
