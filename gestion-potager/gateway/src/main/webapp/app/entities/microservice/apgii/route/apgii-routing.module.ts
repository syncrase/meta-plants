import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { APGIIComponent } from '../list/apgii.component';
import { APGIIDetailComponent } from '../detail/apgii-detail.component';
import { APGIIUpdateComponent } from '../update/apgii-update.component';
import { APGIIRoutingResolveService } from './apgii-routing-resolve.service';

const aPGIIRoute: Routes = [
  {
    path: '',
    component: APGIIComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: APGIIDetailComponent,
    resolve: {
      aPGII: APGIIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: APGIIUpdateComponent,
    resolve: {
      aPGII: APGIIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: APGIIUpdateComponent,
    resolve: {
      aPGII: APGIIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aPGIIRoute)],
  exports: [RouterModule],
})
export class APGIIRoutingModule {}
