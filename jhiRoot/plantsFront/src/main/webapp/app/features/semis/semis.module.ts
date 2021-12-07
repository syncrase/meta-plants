import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SemisRoutingModule } from './semis-routing.module';
import { SemisComponent } from "./list/semis.component";
import { SharedModule } from "../../shared/shared.module";


@NgModule({
  declarations: [
    SemisComponent
  ],
  imports: [
    SharedModule,
    CommonModule,
    SemisRoutingModule
  ]
})
export class SemisModule { }
