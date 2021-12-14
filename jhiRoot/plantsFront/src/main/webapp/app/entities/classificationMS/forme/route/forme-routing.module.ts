import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FormeComponent } from '../list/forme.component';
import { FormeDetailComponent } from '../detail/forme-detail.component';
import { FormeUpdateComponent } from '../update/forme-update.component';
import { FormeRoutingResolveService } from './forme-routing-resolve.service';

const formeRoute: Routes = [
  {
    path: '',
    component: FormeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormeDetailComponent,
    resolve: {
      forme: FormeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormeUpdateComponent,
    resolve: {
      forme: FormeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormeUpdateComponent,
    resolve: {
      forme: FormeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(formeRoute)],
  exports: [RouterModule],
})
export class FormeRoutingModule {}
