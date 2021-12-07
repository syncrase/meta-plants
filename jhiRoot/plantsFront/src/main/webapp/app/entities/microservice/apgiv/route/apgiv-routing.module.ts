import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { APGIVComponent } from '../list/apgiv.component';
import { APGIVDetailComponent } from '../detail/apgiv-detail.component';
import { APGIVUpdateComponent } from '../update/apgiv-update.component';
import { APGIVRoutingResolveService } from './apgiv-routing-resolve.service';

const aPGIVRoute: Routes = [
  {
    path: '',
    component: APGIVComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: APGIVDetailComponent,
    resolve: {
      aPGIV: APGIVRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: APGIVUpdateComponent,
    resolve: {
      aPGIV: APGIVRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: APGIVUpdateComponent,
    resolve: {
      aPGIV: APGIVRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aPGIVRoute)],
  exports: [RouterModule],
})
export class APGIVRoutingModule {}
