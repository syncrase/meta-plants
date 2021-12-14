import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { APGIIPlanteComponent } from '../list/apgii-plante.component';
import { APGIIPlanteDetailComponent } from '../detail/apgii-plante-detail.component';
import { APGIIPlanteUpdateComponent } from '../update/apgii-plante-update.component';
import { APGIIPlanteRoutingResolveService } from './apgii-plante-routing-resolve.service';

const aPGIIPlanteRoute: Routes = [
  {
    path: '',
    component: APGIIPlanteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: APGIIPlanteDetailComponent,
    resolve: {
      aPGIIPlante: APGIIPlanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: APGIIPlanteUpdateComponent,
    resolve: {
      aPGIIPlante: APGIIPlanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: APGIIPlanteUpdateComponent,
    resolve: {
      aPGIIPlante: APGIIPlanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aPGIIPlanteRoute)],
  exports: [RouterModule],
})
export class APGIIPlanteRoutingModule {}
