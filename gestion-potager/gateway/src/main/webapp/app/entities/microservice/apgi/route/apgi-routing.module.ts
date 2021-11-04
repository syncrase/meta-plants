import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { APGIComponent } from '../list/apgi.component';
import { APGIDetailComponent } from '../detail/apgi-detail.component';
import { APGIUpdateComponent } from '../update/apgi-update.component';
import { APGIRoutingResolveService } from './apgi-routing-resolve.service';

const aPGIRoute: Routes = [
  {
    path: '',
    component: APGIComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: APGIDetailComponent,
    resolve: {
      aPGI: APGIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: APGIUpdateComponent,
    resolve: {
      aPGI: APGIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: APGIUpdateComponent,
    resolve: {
      aPGI: APGIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aPGIRoute)],
  exports: [RouterModule],
})
export class APGIRoutingModule {}
