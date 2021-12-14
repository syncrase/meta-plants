import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SuperRegneComponent } from '../list/super-regne.component';
import { SuperRegneDetailComponent } from '../detail/super-regne-detail.component';
import { SuperRegneUpdateComponent } from '../update/super-regne-update.component';
import { SuperRegneRoutingResolveService } from './super-regne-routing-resolve.service';

const superRegneRoute: Routes = [
  {
    path: '',
    component: SuperRegneComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SuperRegneDetailComponent,
    resolve: {
      superRegne: SuperRegneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SuperRegneUpdateComponent,
    resolve: {
      superRegne: SuperRegneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SuperRegneUpdateComponent,
    resolve: {
      superRegne: SuperRegneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(superRegneRoute)],
  exports: [RouterModule],
})
export class SuperRegneRoutingModule {}
