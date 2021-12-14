import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SuperClasseComponent } from '../list/super-classe.component';
import { SuperClasseDetailComponent } from '../detail/super-classe-detail.component';
import { SuperClasseUpdateComponent } from '../update/super-classe-update.component';
import { SuperClasseRoutingResolveService } from './super-classe-routing-resolve.service';

const superClasseRoute: Routes = [
  {
    path: '',
    component: SuperClasseComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SuperClasseDetailComponent,
    resolve: {
      superClasse: SuperClasseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SuperClasseUpdateComponent,
    resolve: {
      superClasse: SuperClasseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SuperClasseUpdateComponent,
    resolve: {
      superClasse: SuperClasseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(superClasseRoute)],
  exports: [RouterModule],
})
export class SuperClasseRoutingModule {}
