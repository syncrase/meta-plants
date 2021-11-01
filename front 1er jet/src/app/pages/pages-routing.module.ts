import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SemisComponent } from './semis/semis.component';
import { ExampleComponent } from './example/example.component';

const routes: Routes = [
  {
    path: 'semis',
    component: SemisComponent
  },
  {
    path: 'example',
    component: ExampleComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PagesRoutingModule { }
