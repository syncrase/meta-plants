import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RaunkierComponent } from '../list/raunkier.component';
import { RaunkierDetailComponent } from '../detail/raunkier-detail.component';
import { RaunkierUpdateComponent } from '../update/raunkier-update.component';
import { RaunkierRoutingResolveService } from './raunkier-routing-resolve.service';

const raunkierRoute: Routes = [
  {
    path: '',
    component: RaunkierComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RaunkierDetailComponent,
    resolve: {
      raunkier: RaunkierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RaunkierUpdateComponent,
    resolve: {
      raunkier: RaunkierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RaunkierUpdateComponent,
    resolve: {
      raunkier: RaunkierRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(raunkierRoute)],
  exports: [RouterModule],
})
export class RaunkierRoutingModule {}
