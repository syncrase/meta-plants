import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MicroEmbranchementComponent } from '../list/micro-embranchement.component';
import { MicroEmbranchementDetailComponent } from '../detail/micro-embranchement-detail.component';
import { MicroEmbranchementUpdateComponent } from '../update/micro-embranchement-update.component';
import { MicroEmbranchementRoutingResolveService } from './micro-embranchement-routing-resolve.service';

const microEmbranchementRoute: Routes = [
  {
    path: '',
    component: MicroEmbranchementComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MicroEmbranchementDetailComponent,
    resolve: {
      microEmbranchement: MicroEmbranchementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MicroEmbranchementUpdateComponent,
    resolve: {
      microEmbranchement: MicroEmbranchementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MicroEmbranchementUpdateComponent,
    resolve: {
      microEmbranchement: MicroEmbranchementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(microEmbranchementRoute)],
  exports: [RouterModule],
})
export class MicroEmbranchementRoutingModule {}
