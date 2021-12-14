import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InfraOrdreComponent } from '../list/infra-ordre.component';
import { InfraOrdreDetailComponent } from '../detail/infra-ordre-detail.component';
import { InfraOrdreUpdateComponent } from '../update/infra-ordre-update.component';
import { InfraOrdreRoutingResolveService } from './infra-ordre-routing-resolve.service';

const infraOrdreRoute: Routes = [
  {
    path: '',
    component: InfraOrdreComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InfraOrdreDetailComponent,
    resolve: {
      infraOrdre: InfraOrdreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InfraOrdreUpdateComponent,
    resolve: {
      infraOrdre: InfraOrdreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InfraOrdreUpdateComponent,
    resolve: {
      infraOrdre: InfraOrdreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(infraOrdreRoute)],
  exports: [RouterModule],
})
export class InfraOrdreRoutingModule {}
