import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SemisRoutingModule } from './semis-routing.module';
import { TableauSemisComponent } from "./list/tableau-semis.component";
import { SharedModule } from "../../shared/shared.module";


@NgModule({
  declarations: [
    TableauSemisComponent
  ],
  imports: [
    SharedModule,
    CommonModule,
    SemisRoutingModule
  ]
})
export class SemisModule { }
