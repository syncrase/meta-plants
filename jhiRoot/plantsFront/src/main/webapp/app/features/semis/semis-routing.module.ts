import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { UserRouteAccessService } from "../../core/auth/user-route-access.service";
import { SemisComponent } from "./list/semis.component";

const routes: Routes = [
  {
    path: '',
    component: SemisComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SemisRoutingModule { }
