import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VarieteComponent } from '../list/variete.component';
import { VarieteDetailComponent } from '../detail/variete-detail.component';
import { VarieteUpdateComponent } from '../update/variete-update.component';
import { VarieteRoutingResolveService } from './variete-routing-resolve.service';

const varieteRoute: Routes = [
  {
    path: '',
    component: VarieteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VarieteDetailComponent,
    resolve: {
      variete: VarieteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VarieteUpdateComponent,
    resolve: {
      variete: VarieteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VarieteUpdateComponent,
    resolve: {
      variete: VarieteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(varieteRoute)],
  exports: [RouterModule],
})
export class VarieteRoutingModule {}
