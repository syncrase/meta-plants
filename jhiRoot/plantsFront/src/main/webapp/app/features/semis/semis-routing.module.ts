import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { UserRouteAccessService } from "../../core/auth/user-route-access.service";
import { TableauSemisComponent } from "./list/tableau-semis.component";

const routes: Routes = [
  {
    path: '',
    component: TableauSemisComponent,
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
