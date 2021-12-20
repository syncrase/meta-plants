import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClassificationCronquistComponent } from '../list/classification-cronquist.component';
import { ClassificationCronquistDetailComponent } from '../detail/classification-cronquist-detail.component';
import { ClassificationCronquistUpdateComponent } from '../update/classification-cronquist-update.component';
import { ClassificationCronquistRoutingResolveService } from './classification-cronquist-routing-resolve.service';

const classificationCronquistRoute: Routes = [
  {
    path: '',
    component: ClassificationCronquistComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClassificationCronquistDetailComponent,
    resolve: {
      classificationCronquist: ClassificationCronquistRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClassificationCronquistUpdateComponent,
    resolve: {
      classificationCronquist: ClassificationCronquistRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClassificationCronquistUpdateComponent,
    resolve: {
      classificationCronquist: ClassificationCronquistRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(classificationCronquistRoute)],
  exports: [RouterModule],
})
export class ClassificationCronquistRoutingModule {}
