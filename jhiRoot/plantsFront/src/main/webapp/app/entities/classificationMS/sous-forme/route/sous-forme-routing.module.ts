import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SousFormeComponent } from '../list/sous-forme.component';
import { SousFormeDetailComponent } from '../detail/sous-forme-detail.component';
import { SousFormeUpdateComponent } from '../update/sous-forme-update.component';
import { SousFormeRoutingResolveService } from './sous-forme-routing-resolve.service';

const sousFormeRoute: Routes = [
  {
    path: '',
    component: SousFormeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SousFormeDetailComponent,
    resolve: {
      sousForme: SousFormeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SousFormeUpdateComponent,
    resolve: {
      sousForme: SousFormeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SousFormeUpdateComponent,
    resolve: {
      sousForme: SousFormeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sousFormeRoute)],
  exports: [RouterModule],
})
export class SousFormeRoutingModule {}
