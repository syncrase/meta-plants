import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SousSectionComponent } from '../list/sous-section.component';
import { SousSectionDetailComponent } from '../detail/sous-section-detail.component';
import { SousSectionUpdateComponent } from '../update/sous-section-update.component';
import { SousSectionRoutingResolveService } from './sous-section-routing-resolve.service';

const sousSectionRoute: Routes = [
  {
    path: '',
    component: SousSectionComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SousSectionDetailComponent,
    resolve: {
      sousSection: SousSectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SousSectionUpdateComponent,
    resolve: {
      sousSection: SousSectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SousSectionUpdateComponent,
    resolve: {
      sousSection: SousSectionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sousSectionRoute)],
  exports: [RouterModule],
})
export class SousSectionRoutingModule {}
