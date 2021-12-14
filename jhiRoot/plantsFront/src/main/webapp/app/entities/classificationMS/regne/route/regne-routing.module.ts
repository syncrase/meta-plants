import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RegneComponent } from '../list/regne.component';
import { RegneDetailComponent } from '../detail/regne-detail.component';
import { RegneUpdateComponent } from '../update/regne-update.component';
import { RegneRoutingResolveService } from './regne-routing-resolve.service';

const regneRoute: Routes = [
  {
    path: '',
    component: RegneComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RegneDetailComponent,
    resolve: {
      regne: RegneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RegneUpdateComponent,
    resolve: {
      regne: RegneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RegneUpdateComponent,
    resolve: {
      regne: RegneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(regneRoute)],
  exports: [RouterModule],
})
export class RegneRoutingModule {}
