import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { APGIVPlanteComponent } from '../list/apgiv-plante.component';
import { APGIVPlanteDetailComponent } from '../detail/apgiv-plante-detail.component';
import { APGIVPlanteUpdateComponent } from '../update/apgiv-plante-update.component';
import { APGIVPlanteRoutingResolveService } from './apgiv-plante-routing-resolve.service';

const aPGIVPlanteRoute: Routes = [
  {
    path: '',
    component: APGIVPlanteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: APGIVPlanteDetailComponent,
    resolve: {
      aPGIVPlante: APGIVPlanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: APGIVPlanteUpdateComponent,
    resolve: {
      aPGIVPlante: APGIVPlanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: APGIVPlanteUpdateComponent,
    resolve: {
      aPGIVPlante: APGIVPlanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aPGIVPlanteRoute)],
  exports: [RouterModule],
})
export class APGIVPlanteRoutingModule {}
