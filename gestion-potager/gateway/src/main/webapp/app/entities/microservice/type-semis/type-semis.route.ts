import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITypeSemis, TypeSemis } from 'app/shared/model/microservice/type-semis.model';
import { TypeSemisService } from './type-semis.service';
import { TypeSemisComponent } from './type-semis.component';
import { TypeSemisDetailComponent } from './type-semis-detail.component';
import { TypeSemisUpdateComponent } from './type-semis-update.component';

@Injectable({ providedIn: 'root' })
export class TypeSemisResolve implements Resolve<ITypeSemis> {
  constructor(private service: TypeSemisService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITypeSemis> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((typeSemis: HttpResponse<TypeSemis>) => {
          if (typeSemis.body) {
            return of(typeSemis.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TypeSemis());
  }
}

export const typeSemisRoute: Routes = [
  {
    path: '',
    component: TypeSemisComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gatewayApp.microserviceTypeSemis.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeSemisDetailComponent,
    resolve: {
      typeSemis: TypeSemisResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceTypeSemis.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeSemisUpdateComponent,
    resolve: {
      typeSemis: TypeSemisResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceTypeSemis.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeSemisUpdateComponent,
    resolve: {
      typeSemis: TypeSemisResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceTypeSemis.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
