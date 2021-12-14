import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SousRegneComponent } from '../list/sous-regne.component';
import { SousRegneDetailComponent } from '../detail/sous-regne-detail.component';
import { SousRegneUpdateComponent } from '../update/sous-regne-update.component';
import { SousRegneRoutingResolveService } from './sous-regne-routing-resolve.service';

const sousRegneRoute: Routes = [
  {
    path: '',
    component: SousRegneComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SousRegneDetailComponent,
    resolve: {
      sousRegne: SousRegneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SousRegneUpdateComponent,
    resolve: {
      sousRegne: SousRegneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SousRegneUpdateComponent,
    resolve: {
      sousRegne: SousRegneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sousRegneRoute)],
  exports: [RouterModule],
})
export class SousRegneRoutingModule {}
