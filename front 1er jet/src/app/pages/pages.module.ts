import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PagesRoutingModule } from './pages-routing.module';
import { ExampleComponent } from './example/example.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { SemisComponent } from './semis/semis.component';
import { SortableHeader } from './semis/sortable-header.directive';
import { FiltresComponent } from './semis/filtres/filtres.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MethodeSemisComponent } from './semis/filtres/methode-semis/methode-semis.component';
import { PeriodeComponent } from './semis/filtres/periode/periode.component';

@NgModule({
  declarations: [
    ExampleComponent,
    SemisComponent,
    SortableHeader,
    FiltresComponent,
    MethodeSemisComponent,
    PeriodeComponent
  ],
  imports: [
    CommonModule,
    PagesRoutingModule,
    NgbModule,
    ReactiveFormsModule
  ],
  bootstrap: [SemisComponent]
})
export class PagesModule { }
