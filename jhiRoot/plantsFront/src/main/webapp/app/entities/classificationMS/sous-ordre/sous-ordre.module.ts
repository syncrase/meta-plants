import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SousOrdreComponent } from './list/sous-ordre.component';
import { SousOrdreDetailComponent } from './detail/sous-ordre-detail.component';
import { SousOrdreUpdateComponent } from './update/sous-ordre-update.component';
import { SousOrdreDeleteDialogComponent } from './delete/sous-ordre-delete-dialog.component';
import { SousOrdreRoutingModule } from './route/sous-ordre-routing.module';

@NgModule({
  imports: [SharedModule, SousOrdreRoutingModule],
  declarations: [SousOrdreComponent, SousOrdreDetailComponent, SousOrdreUpdateComponent, SousOrdreDeleteDialogComponent],
  entryComponents: [SousOrdreDeleteDialogComponent],
})
export class ClassificationMsSousOrdreModule {}
