import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RameauComponent } from '../list/rameau.component';
import { RameauDetailComponent } from '../detail/rameau-detail.component';
import { RameauUpdateComponent } from '../update/rameau-update.component';
import { RameauRoutingResolveService } from './rameau-routing-resolve.service';

const rameauRoute: Routes = [
  {
    path: '',
    component: RameauComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RameauDetailComponent,
    resolve: {
      rameau: RameauRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RameauUpdateComponent,
    resolve: {
      rameau: RameauRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RameauUpdateComponent,
    resolve: {
      rameau: RameauRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rameauRoute)],
  exports: [RouterModule],
})
export class RameauRoutingModule {}
