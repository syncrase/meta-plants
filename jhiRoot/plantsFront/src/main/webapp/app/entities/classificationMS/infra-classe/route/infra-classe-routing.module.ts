import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InfraClasseComponent } from '../list/infra-classe.component';
import { InfraClasseDetailComponent } from '../detail/infra-classe-detail.component';
import { InfraClasseUpdateComponent } from '../update/infra-classe-update.component';
import { InfraClasseRoutingResolveService } from './infra-classe-routing-resolve.service';

const infraClasseRoute: Routes = [
  {
    path: '',
    component: InfraClasseComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InfraClasseDetailComponent,
    resolve: {
      infraClasse: InfraClasseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InfraClasseUpdateComponent,
    resolve: {
      infraClasse: InfraClasseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InfraClasseUpdateComponent,
    resolve: {
      infraClasse: InfraClasseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(infraClasseRoute)],
  exports: [RouterModule],
})
export class InfraClasseRoutingModule {}
