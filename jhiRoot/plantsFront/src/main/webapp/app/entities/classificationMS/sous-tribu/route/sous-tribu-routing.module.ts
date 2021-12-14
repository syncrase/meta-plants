import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SousTribuComponent } from '../list/sous-tribu.component';
import { SousTribuDetailComponent } from '../detail/sous-tribu-detail.component';
import { SousTribuUpdateComponent } from '../update/sous-tribu-update.component';
import { SousTribuRoutingResolveService } from './sous-tribu-routing-resolve.service';

const sousTribuRoute: Routes = [
  {
    path: '',
    component: SousTribuComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SousTribuDetailComponent,
    resolve: {
      sousTribu: SousTribuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SousTribuUpdateComponent,
    resolve: {
      sousTribu: SousTribuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SousTribuUpdateComponent,
    resolve: {
      sousTribu: SousTribuRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sousTribuRoute)],
  exports: [RouterModule],
})
export class SousTribuRoutingModule {}
