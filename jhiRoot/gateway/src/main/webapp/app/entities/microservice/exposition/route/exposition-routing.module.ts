import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExpositionComponent } from '../list/exposition.component';
import { ExpositionDetailComponent } from '../detail/exposition-detail.component';
import { ExpositionUpdateComponent } from '../update/exposition-update.component';
import { ExpositionRoutingResolveService } from './exposition-routing-resolve.service';

const expositionRoute: Routes = [
  {
    path: '',
    component: ExpositionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExpositionDetailComponent,
    resolve: {
      exposition: ExpositionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExpositionUpdateComponent,
    resolve: {
      exposition: ExpositionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExpositionUpdateComponent,
    resolve: {
      exposition: ExpositionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(expositionRoute)],
  exports: [RouterModule],
})
export class ExpositionRoutingModule {}
