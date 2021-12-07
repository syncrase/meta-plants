import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { APGIIIComponent } from '../list/apgiii.component';
import { APGIIIDetailComponent } from '../detail/apgiii-detail.component';
import { APGIIIUpdateComponent } from '../update/apgiii-update.component';
import { APGIIIRoutingResolveService } from './apgiii-routing-resolve.service';

const aPGIIIRoute: Routes = [
  {
    path: '',
    component: APGIIIComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: APGIIIDetailComponent,
    resolve: {
      aPGIII: APGIIIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: APGIIIUpdateComponent,
    resolve: {
      aPGIII: APGIIIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: APGIIIUpdateComponent,
    resolve: {
      aPGIII: APGIIIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aPGIIIRoute)],
  exports: [RouterModule],
})
export class APGIIIRoutingModule {}
