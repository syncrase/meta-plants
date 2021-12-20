import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ClassificationCronquistComponent } from './list/classification-cronquist.component';
import { ClassificationCronquistDetailComponent } from './detail/classification-cronquist-detail.component';
import { ClassificationCronquistUpdateComponent } from './update/classification-cronquist-update.component';
import { ClassificationCronquistDeleteDialogComponent } from './delete/classification-cronquist-delete-dialog.component';
import { ClassificationCronquistRoutingModule } from './route/classification-cronquist-routing.module';

@NgModule({
  imports: [SharedModule, ClassificationCronquistRoutingModule],
  declarations: [
    ClassificationCronquistComponent,
    ClassificationCronquistDetailComponent,
    ClassificationCronquistUpdateComponent,
    ClassificationCronquistDeleteDialogComponent,
  ],
  entryComponents: [ClassificationCronquistDeleteDialogComponent],
})
export class PlantsMsClassificationCronquistModule {}
