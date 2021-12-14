import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InfraEmbranchementComponent } from '../list/infra-embranchement.component';
import { InfraEmbranchementDetailComponent } from '../detail/infra-embranchement-detail.component';
import { InfraEmbranchementUpdateComponent } from '../update/infra-embranchement-update.component';
import { InfraEmbranchementRoutingResolveService } from './infra-embranchement-routing-resolve.service';

const infraEmbranchementRoute: Routes = [
  {
    path: '',
    component: InfraEmbranchementComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InfraEmbranchementDetailComponent,
    resolve: {
      infraEmbranchement: InfraEmbranchementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InfraEmbranchementUpdateComponent,
    resolve: {
      infraEmbranchement: InfraEmbranchementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InfraEmbranchementUpdateComponent,
    resolve: {
      infraEmbranchement: InfraEmbranchementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(infraEmbranchementRoute)],
  exports: [RouterModule],
})
export class InfraEmbranchementRoutingModule {}
