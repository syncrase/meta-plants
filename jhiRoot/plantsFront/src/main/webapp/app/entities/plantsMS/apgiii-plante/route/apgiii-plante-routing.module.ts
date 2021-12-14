import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { APGIIIPlanteComponent } from '../list/apgiii-plante.component';
import { APGIIIPlanteDetailComponent } from '../detail/apgiii-plante-detail.component';
import { APGIIIPlanteUpdateComponent } from '../update/apgiii-plante-update.component';
import { APGIIIPlanteRoutingResolveService } from './apgiii-plante-routing-resolve.service';

const aPGIIIPlanteRoute: Routes = [
  {
    path: '',
    component: APGIIIPlanteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: APGIIIPlanteDetailComponent,
    resolve: {
      aPGIIIPlante: APGIIIPlanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: APGIIIPlanteUpdateComponent,
    resolve: {
      aPGIIIPlante: APGIIIPlanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: APGIIIPlanteUpdateComponent,
    resolve: {
      aPGIIIPlante: APGIIIPlanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aPGIIIPlanteRoute)],
  exports: [RouterModule],
})
export class APGIIIPlanteRoutingModule {}
