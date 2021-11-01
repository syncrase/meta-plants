import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { ClassificationComponent } from './classification.component';
import { ClassificationDetailComponent } from './classification-detail.component';
import { ClassificationUpdateComponent } from './classification-update.component';
import { ClassificationDeleteDialogComponent } from './classification-delete-dialog.component';
import { classificationRoute } from './classification.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(classificationRoute)],
  declarations: [
    ClassificationComponent,
    ClassificationDetailComponent,
    ClassificationUpdateComponent,
    ClassificationDeleteDialogComponent,
  ],
  entryComponents: [ClassificationDeleteDialogComponent],
})
export class MicroserviceClassificationModule {}
