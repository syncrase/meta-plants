import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClassificationNomComponent } from '../list/classification-nom.component';
import { ClassificationNomDetailComponent } from '../detail/classification-nom-detail.component';
import { ClassificationNomUpdateComponent } from '../update/classification-nom-update.component';
import { ClassificationNomRoutingResolveService } from './classification-nom-routing-resolve.service';

const classificationNomRoute: Routes = [
  {
    path: '',
    component: ClassificationNomComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClassificationNomDetailComponent,
    resolve: {
      classificationNom: ClassificationNomRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClassificationNomUpdateComponent,
    resolve: {
      classificationNom: ClassificationNomRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClassificationNomUpdateComponent,
    resolve: {
      classificationNom: ClassificationNomRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(classificationNomRoute)],
  exports: [RouterModule],
})
export class ClassificationNomRoutingModule {}
