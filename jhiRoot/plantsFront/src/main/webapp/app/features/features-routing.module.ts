import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'tableausemis',
    data: { pageTitle: 'Tableau des semis' },
    loadChildren: () => import('./semis/semis.module').then(m => m.SemisModule),
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FeaturesRoutingModule { }
