import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { INomVernaculaire, NomVernaculaire } from 'app/shared/model/microservice/nom-vernaculaire.model';
import { NomVernaculaireService } from './nom-vernaculaire.service';
import { NomVernaculaireComponent } from './nom-vernaculaire.component';
import { NomVernaculaireDetailComponent } from './nom-vernaculaire-detail.component';
import { NomVernaculaireUpdateComponent } from './nom-vernaculaire-update.component';

@Injectable({ providedIn: 'root' })
export class NomVernaculaireResolve implements Resolve<INomVernaculaire> {
  constructor(private service: NomVernaculaireService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INomVernaculaire> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((nomVernaculaire: HttpResponse<NomVernaculaire>) => {
          if (nomVernaculaire.body) {
            return of(nomVernaculaire.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NomVernaculaire());
  }
}

export const nomVernaculaireRoute: Routes = [
  {
    path: '',
    component: NomVernaculaireComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'gatewayApp.microserviceNomVernaculaire.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NomVernaculaireDetailComponent,
    resolve: {
      nomVernaculaire: NomVernaculaireResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceNomVernaculaire.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NomVernaculaireUpdateComponent,
    resolve: {
      nomVernaculaire: NomVernaculaireResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceNomVernaculaire.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NomVernaculaireUpdateComponent,
    resolve: {
      nomVernaculaire: NomVernaculaireResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'gatewayApp.microserviceNomVernaculaire.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
