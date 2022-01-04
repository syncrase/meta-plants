import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ClassificationNomComponent } from './list/classification-nom.component';
import { ClassificationNomDetailComponent } from './detail/classification-nom-detail.component';
import { ClassificationNomUpdateComponent } from './update/classification-nom-update.component';
import { ClassificationNomDeleteDialogComponent } from './delete/classification-nom-delete-dialog.component';
import { ClassificationNomRoutingModule } from './route/classification-nom-routing.module';

@NgModule({
  imports: [SharedModule, ClassificationNomRoutingModule],
  declarations: [
    ClassificationNomComponent,
    ClassificationNomDetailComponent,
    ClassificationNomUpdateComponent,
    ClassificationNomDeleteDialogComponent,
  ],
  entryComponents: [ClassificationNomDeleteDialogComponent],
})
export class ClassificationMsClassificationNomModule {}
