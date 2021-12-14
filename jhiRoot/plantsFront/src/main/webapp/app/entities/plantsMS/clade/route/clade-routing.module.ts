import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CladeComponent } from '../list/clade.component';
import { CladeDetailComponent } from '../detail/clade-detail.component';
import { CladeUpdateComponent } from '../update/clade-update.component';
import { CladeRoutingResolveService } from './clade-routing-resolve.service';

const cladeRoute: Routes = [
  {
    path: '',
    component: CladeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CladeDetailComponent,
    resolve: {
      clade: CladeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CladeUpdateComponent,
    resolve: {
      clade: CladeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CladeUpdateComponent,
    resolve: {
      clade: CladeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cladeRoute)],
  exports: [RouterModule],
})
export class CladeRoutingModule {}
