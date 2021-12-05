import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CronquistComponent } from '../list/cronquist.component';
import { CronquistDetailComponent } from '../detail/cronquist-detail.component';
import { CronquistUpdateComponent } from '../update/cronquist-update.component';
import { CronquistRoutingResolveService } from './cronquist-routing-resolve.service';

const cronquistRoute: Routes = [
  {
    path: '',
    component: CronquistComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CronquistDetailComponent,
    resolve: {
      cronquist: CronquistRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CronquistUpdateComponent,
    resolve: {
      cronquist: CronquistRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CronquistUpdateComponent,
    resolve: {
      cronquist: CronquistRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cronquistRoute)],
  exports: [RouterModule],
})
export class CronquistRoutingModule {}
