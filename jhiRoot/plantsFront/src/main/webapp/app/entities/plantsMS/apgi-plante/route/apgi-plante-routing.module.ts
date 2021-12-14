import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { APGIPlanteComponent } from '../list/apgi-plante.component';
import { APGIPlanteDetailComponent } from '../detail/apgi-plante-detail.component';
import { APGIPlanteUpdateComponent } from '../update/apgi-plante-update.component';
import { APGIPlanteRoutingResolveService } from './apgi-plante-routing-resolve.service';

const aPGIPlanteRoute: Routes = [
  {
    path: '',
    component: APGIPlanteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: APGIPlanteDetailComponent,
    resolve: {
      aPGIPlante: APGIPlanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: APGIPlanteUpdateComponent,
    resolve: {
      aPGIPlante: APGIPlanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: APGIPlanteUpdateComponent,
    resolve: {
      aPGIPlante: APGIPlanteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aPGIPlanteRoute)],
  exports: [RouterModule],
})
export class APGIPlanteRoutingModule {}
