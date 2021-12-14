import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RaunkierPlanteComponent } from '../list/raunkier-plante.component';
import { RaunkierPlanteDetailComponent } from '../detail/raunkier-plante-detail.component';
import { RaunkierPlanteUpdateComponent } from '../update/raunkier-plante-update.component';
import { RaunkierPlanteRoutingResolveService } from './raunkier-plante-routing-resolve.service';

const raunkierPlanteRoute: Routes = [
  {
    path: '',
    component: RaunkierPlanteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RaunkierPlanteDetailComponent,
    resolve: {
      raunkierPlante: RaunkierPlanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RaunkierPlanteUpdateComponent,
    resolve: {
      raunkierPlante: RaunkierPlanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RaunkierPlanteUpdateComponent,
    resolve: {
      raunkierPlante: RaunkierPlanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(raunkierPlanteRoute)],
  exports: [RouterModule],
})
export class RaunkierPlanteRoutingModule {}
