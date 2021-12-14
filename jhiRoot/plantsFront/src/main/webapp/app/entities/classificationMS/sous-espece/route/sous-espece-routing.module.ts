import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SousEspeceComponent } from '../list/sous-espece.component';
import { SousEspeceDetailComponent } from '../detail/sous-espece-detail.component';
import { SousEspeceUpdateComponent } from '../update/sous-espece-update.component';
import { SousEspeceRoutingResolveService } from './sous-espece-routing-resolve.service';

const sousEspeceRoute: Routes = [
  {
    path: '',
    component: SousEspeceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SousEspeceDetailComponent,
    resolve: {
      sousEspece: SousEspeceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SousEspeceUpdateComponent,
    resolve: {
      sousEspece: SousEspeceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SousEspeceUpdateComponent,
    resolve: {
      sousEspece: SousEspeceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sousEspeceRoute)],
  exports: [RouterModule],
})
export class SousEspeceRoutingModule {}
