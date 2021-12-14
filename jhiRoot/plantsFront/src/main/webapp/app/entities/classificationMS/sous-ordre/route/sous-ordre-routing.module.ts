import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SousOrdreComponent } from '../list/sous-ordre.component';
import { SousOrdreDetailComponent } from '../detail/sous-ordre-detail.component';
import { SousOrdreUpdateComponent } from '../update/sous-ordre-update.component';
import { SousOrdreRoutingResolveService } from './sous-ordre-routing-resolve.service';

const sousOrdreRoute: Routes = [
  {
    path: '',
    component: SousOrdreComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SousOrdreDetailComponent,
    resolve: {
      sousOrdre: SousOrdreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SousOrdreUpdateComponent,
    resolve: {
      sousOrdre: SousOrdreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SousOrdreUpdateComponent,
    resolve: {
      sousOrdre: SousOrdreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sousOrdreRoute)],
  exports: [RouterModule],
})
export class SousOrdreRoutingModule {}
