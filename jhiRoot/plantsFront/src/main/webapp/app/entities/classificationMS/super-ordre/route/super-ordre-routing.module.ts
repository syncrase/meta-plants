import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SuperOrdreComponent } from '../list/super-ordre.component';
import { SuperOrdreDetailComponent } from '../detail/super-ordre-detail.component';
import { SuperOrdreUpdateComponent } from '../update/super-ordre-update.component';
import { SuperOrdreRoutingResolveService } from './super-ordre-routing-resolve.service';

const superOrdreRoute: Routes = [
  {
    path: '',
    component: SuperOrdreComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SuperOrdreDetailComponent,
    resolve: {
      superOrdre: SuperOrdreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SuperOrdreUpdateComponent,
    resolve: {
      superOrdre: SuperOrdreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SuperOrdreUpdateComponent,
    resolve: {
      superOrdre: SuperOrdreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(superOrdreRoute)],
  exports: [RouterModule],
})
export class SuperOrdreRoutingModule {}
