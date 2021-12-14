import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OrdreComponent } from './list/ordre.component';
import { OrdreDetailComponent } from './detail/ordre-detail.component';
import { OrdreUpdateComponent } from './update/ordre-update.component';
import { OrdreDeleteDialogComponent } from './delete/ordre-delete-dialog.component';
import { OrdreRoutingModule } from './route/ordre-routing.module';

@NgModule({
  imports: [SharedModule, OrdreRoutingModule],
  declarations: [OrdreComponent, OrdreDetailComponent, OrdreUpdateComponent, OrdreDeleteDialogComponent],
  entryComponents: [OrdreDeleteDialogComponent],
})
export class ClassificationMsOrdreModule {}
