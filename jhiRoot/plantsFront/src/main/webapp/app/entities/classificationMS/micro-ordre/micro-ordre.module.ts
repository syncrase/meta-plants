import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MicroOrdreComponent } from './list/micro-ordre.component';
import { MicroOrdreDetailComponent } from './detail/micro-ordre-detail.component';
import { MicroOrdreUpdateComponent } from './update/micro-ordre-update.component';
import { MicroOrdreDeleteDialogComponent } from './delete/micro-ordre-delete-dialog.component';
import { MicroOrdreRoutingModule } from './route/micro-ordre-routing.module';

@NgModule({
  imports: [SharedModule, MicroOrdreRoutingModule],
  declarations: [MicroOrdreComponent, MicroOrdreDetailComponent, MicroOrdreUpdateComponent, MicroOrdreDeleteDialogComponent],
  entryComponents: [MicroOrdreDeleteDialogComponent],
})
export class ClassificationMsMicroOrdreModule {}
