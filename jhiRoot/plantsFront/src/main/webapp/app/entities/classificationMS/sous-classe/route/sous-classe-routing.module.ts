import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SousClasseComponent } from '../list/sous-classe.component';
import { SousClasseDetailComponent } from '../detail/sous-classe-detail.component';
import { SousClasseUpdateComponent } from '../update/sous-classe-update.component';
import { SousClasseRoutingResolveService } from './sous-classe-routing-resolve.service';

const sousClasseRoute: Routes = [
  {
    path: '',
    component: SousClasseComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SousClasseDetailComponent,
    resolve: {
      sousClasse: SousClasseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SousClasseUpdateComponent,
    resolve: {
      sousClasse: SousClasseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SousClasseUpdateComponent,
    resolve: {
      sousClasse: SousClasseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sousClasseRoute)],
  exports: [RouterModule],
})
export class SousClasseRoutingModule {}
