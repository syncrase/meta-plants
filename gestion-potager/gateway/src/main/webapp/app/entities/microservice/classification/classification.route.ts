import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IClassification, Classification } from 'app/shared/model/microservice/classification.model';
import { ClassificationService } from './classification.service';
import { ClassificationComponent } from './classification.component';
import { ClassificationDetailComponent } from './classification-detail.component';
import { ClassificationUpdateComponent } from './classification-update.component';

@Injectable({ providedIn: 'root' })
export class ClassificationResolve implements Resolve<IClassification> {
  constructor(private service: ClassificationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClassification> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((classification: HttpResponse<Classification>) => {
          if (classification.body) {
            return of(classification.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Classification());
  }
}

export const classificationRoute: Routes = [
  {
    path: '',
    component: ClassificationComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gatewayApp.microserviceClassification.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClassificationDetailComponent,
    resolve: {
      classification: ClassificationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceClassification.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClassificationUpdateComponent,
    resolve: {
      classification: ClassificationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceClassification.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClassificationUpdateComponent,
    resolve: {
      classification: ClassificationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceClassification.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
