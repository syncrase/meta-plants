import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ExpositionComponent } from './list/exposition.component';
import { ExpositionDetailComponent } from './detail/exposition-detail.component';
import { ExpositionUpdateComponent } from './update/exposition-update.component';
import { ExpositionDeleteDialogComponent } from './delete/exposition-delete-dialog.component';
import { ExpositionRoutingModule } from './route/exposition-routing.module';

@NgModule({
  imports: [SharedModule, ExpositionRoutingModule],
  declarations: [ExpositionComponent, ExpositionDetailComponent, ExpositionUpdateComponent, ExpositionDeleteDialogComponent],
  entryComponents: [ExpositionDeleteDialogComponent],
})
export class MicroserviceExpositionModule {}
