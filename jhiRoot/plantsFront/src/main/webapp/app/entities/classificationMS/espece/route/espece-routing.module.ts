import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EspeceComponent } from '../list/espece.component';
import { EspeceDetailComponent } from '../detail/espece-detail.component';
import { EspeceUpdateComponent } from '../update/espece-update.component';
import { EspeceRoutingResolveService } from './espece-routing-resolve.service';

const especeRoute: Routes = [
  {
    path: '',
    component: EspeceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EspeceDetailComponent,
    resolve: {
      espece: EspeceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EspeceUpdateComponent,
    resolve: {
      espece: EspeceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EspeceUpdateComponent,
    resolve: {
      espece: EspeceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(especeRoute)],
  exports: [RouterModule],
})
export class EspeceRoutingModule {}
