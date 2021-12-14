import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TribuComponent } from '../list/tribu.component';
import { TribuDetailComponent } from '../detail/tribu-detail.component';
import { TribuUpdateComponent } from '../update/tribu-update.component';
import { TribuRoutingResolveService } from './tribu-routing-resolve.service';

const tribuRoute: Routes = [
  {
    path: '',
    component: TribuComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TribuDetailComponent,
    resolve: {
      tribu: TribuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TribuUpdateComponent,
    resolve: {
      tribu: TribuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TribuUpdateComponent,
    resolve: {
      tribu: TribuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tribuRoute)],
  exports: [RouterModule],
})
export class TribuRoutingModule {}
