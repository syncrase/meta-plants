import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CronquistPlanteComponent } from '../list/cronquist-plante.component';
import { CronquistPlanteDetailComponent } from '../detail/cronquist-plante-detail.component';
import { CronquistPlanteUpdateComponent } from '../update/cronquist-plante-update.component';
import { CronquistPlanteRoutingResolveService } from './cronquist-plante-routing-resolve.service';

const cronquistPlanteRoute: Routes = [
  {
    path: '',
    component: CronquistPlanteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CronquistPlanteDetailComponent,
    resolve: {
      cronquistPlante: CronquistPlanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CronquistPlanteUpdateComponent,
    resolve: {
      cronquistPlante: CronquistPlanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CronquistPlanteUpdateComponent,
    resolve: {
      cronquistPlante: CronquistPlanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cronquistPlanteRoute)],
  exports: [RouterModule],
})
export class CronquistPlanteRoutingModule {}
