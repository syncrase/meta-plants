import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SousGenreComponent } from '../list/sous-genre.component';
import { SousGenreDetailComponent } from '../detail/sous-genre-detail.component';
import { SousGenreUpdateComponent } from '../update/sous-genre-update.component';
import { SousGenreRoutingResolveService } from './sous-genre-routing-resolve.service';

const sousGenreRoute: Routes = [
  {
    path: '',
    component: SousGenreComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SousGenreDetailComponent,
    resolve: {
      sousGenre: SousGenreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SousGenreUpdateComponent,
    resolve: {
      sousGenre: SousGenreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SousGenreUpdateComponent,
    resolve: {
      sousGenre: SousGenreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sousGenreRoute)],
  exports: [RouterModule],
})
export class SousGenreRoutingModule {}
