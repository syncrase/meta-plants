import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SousDivisionComponent } from '../list/sous-division.component';
import { SousDivisionDetailComponent } from '../detail/sous-division-detail.component';
import { SousDivisionUpdateComponent } from '../update/sous-division-update.component';
import { SousDivisionRoutingResolveService } from './sous-division-routing-resolve.service';

const sousDivisionRoute: Routes = [
  {
    path: '',
    component: SousDivisionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SousDivisionDetailComponent,
    resolve: {
      sousDivision: SousDivisionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SousDivisionUpdateComponent,
    resolve: {
      sousDivision: SousDivisionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SousDivisionUpdateComponent,
    resolve: {
      sousDivision: SousDivisionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sousDivisionRoute)],
  exports: [RouterModule],
})
export class SousDivisionRoutingModule {}
