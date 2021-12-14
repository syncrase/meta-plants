import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SousVarieteComponent } from '../list/sous-variete.component';
import { SousVarieteDetailComponent } from '../detail/sous-variete-detail.component';
import { SousVarieteUpdateComponent } from '../update/sous-variete-update.component';
import { SousVarieteRoutingResolveService } from './sous-variete-routing-resolve.service';

const sousVarieteRoute: Routes = [
  {
    path: '',
    component: SousVarieteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SousVarieteDetailComponent,
    resolve: {
      sousVariete: SousVarieteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SousVarieteUpdateComponent,
    resolve: {
      sousVariete: SousVarieteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SousVarieteUpdateComponent,
    resolve: {
      sousVariete: SousVarieteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sousVarieteRoute)],
  exports: [RouterModule],
})
export class SousVarieteRoutingModule {}
