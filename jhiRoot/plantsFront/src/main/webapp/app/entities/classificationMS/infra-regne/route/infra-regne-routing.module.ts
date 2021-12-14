import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InfraRegneComponent } from '../list/infra-regne.component';
import { InfraRegneDetailComponent } from '../detail/infra-regne-detail.component';
import { InfraRegneUpdateComponent } from '../update/infra-regne-update.component';
import { InfraRegneRoutingResolveService } from './infra-regne-routing-resolve.service';

const infraRegneRoute: Routes = [
  {
    path: '',
    component: InfraRegneComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InfraRegneDetailComponent,
    resolve: {
      infraRegne: InfraRegneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InfraRegneUpdateComponent,
    resolve: {
      infraRegne: InfraRegneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InfraRegneUpdateComponent,
    resolve: {
      infraRegne: InfraRegneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(infraRegneRoute)],
  exports: [RouterModule],
})
export class InfraRegneRoutingModule {}
