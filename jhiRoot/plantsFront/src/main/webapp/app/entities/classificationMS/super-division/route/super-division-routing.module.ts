import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SuperDivisionComponent } from '../list/super-division.component';
import { SuperDivisionDetailComponent } from '../detail/super-division-detail.component';
import { SuperDivisionUpdateComponent } from '../update/super-division-update.component';
import { SuperDivisionRoutingResolveService } from './super-division-routing-resolve.service';

const superDivisionRoute: Routes = [
  {
    path: '',
    component: SuperDivisionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SuperDivisionDetailComponent,
    resolve: {
      superDivision: SuperDivisionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SuperDivisionUpdateComponent,
    resolve: {
      superDivision: SuperDivisionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SuperDivisionUpdateComponent,
    resolve: {
      superDivision: SuperDivisionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(superDivisionRoute)],
  exports: [RouterModule],
})
export class SuperDivisionRoutingModule {}
