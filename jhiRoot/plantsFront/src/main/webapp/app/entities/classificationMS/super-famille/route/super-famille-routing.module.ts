import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SuperFamilleComponent } from '../list/super-famille.component';
import { SuperFamilleDetailComponent } from '../detail/super-famille-detail.component';
import { SuperFamilleUpdateComponent } from '../update/super-famille-update.component';
import { SuperFamilleRoutingResolveService } from './super-famille-routing-resolve.service';

const superFamilleRoute: Routes = [
  {
    path: '',
    component: SuperFamilleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SuperFamilleDetailComponent,
    resolve: {
      superFamille: SuperFamilleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SuperFamilleUpdateComponent,
    resolve: {
      superFamille: SuperFamilleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SuperFamilleUpdateComponent,
    resolve: {
      superFamille: SuperFamilleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(superFamilleRoute)],
  exports: [RouterModule],
})
export class SuperFamilleRoutingModule {}
