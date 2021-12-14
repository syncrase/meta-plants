import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SuperOrdreComponent } from './list/super-ordre.component';
import { SuperOrdreDetailComponent } from './detail/super-ordre-detail.component';
import { SuperOrdreUpdateComponent } from './update/super-ordre-update.component';
import { SuperOrdreDeleteDialogComponent } from './delete/super-ordre-delete-dialog.component';
import { SuperOrdreRoutingModule } from './route/super-ordre-routing.module';

@NgModule({
  imports: [SharedModule, SuperOrdreRoutingModule],
  declarations: [SuperOrdreComponent, SuperOrdreDetailComponent, SuperOrdreUpdateComponent, SuperOrdreDeleteDialogComponent],
  entryComponents: [SuperOrdreDeleteDialogComponent],
})
export class ClassificationMsSuperOrdreModule {}
