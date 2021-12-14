import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrdreComponent } from '../list/ordre.component';
import { OrdreDetailComponent } from '../detail/ordre-detail.component';
import { OrdreUpdateComponent } from '../update/ordre-update.component';
import { OrdreRoutingResolveService } from './ordre-routing-resolve.service';

const ordreRoute: Routes = [
  {
    path: '',
    component: OrdreComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrdreDetailComponent,
    resolve: {
      ordre: OrdreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrdreUpdateComponent,
    resolve: {
      ordre: OrdreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrdreUpdateComponent,
    resolve: {
      ordre: OrdreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ordreRoute)],
  exports: [RouterModule],
})
export class OrdreRoutingModule {}
