import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MicroOrdreComponent } from '../list/micro-ordre.component';
import { MicroOrdreDetailComponent } from '../detail/micro-ordre-detail.component';
import { MicroOrdreUpdateComponent } from '../update/micro-ordre-update.component';
import { MicroOrdreRoutingResolveService } from './micro-ordre-routing-resolve.service';

const microOrdreRoute: Routes = [
  {
    path: '',
    component: MicroOrdreComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MicroOrdreDetailComponent,
    resolve: {
      microOrdre: MicroOrdreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MicroOrdreUpdateComponent,
    resolve: {
      microOrdre: MicroOrdreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MicroOrdreUpdateComponent,
    resolve: {
      microOrdre: MicroOrdreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(microOrdreRoute)],
  exports: [RouterModule],
})
export class MicroOrdreRoutingModule {}
