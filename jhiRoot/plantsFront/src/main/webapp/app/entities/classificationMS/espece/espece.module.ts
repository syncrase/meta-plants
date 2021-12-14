import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EspeceComponent } from './list/espece.component';
import { EspeceDetailComponent } from './detail/espece-detail.component';
import { EspeceUpdateComponent } from './update/espece-update.component';
import { EspeceDeleteDialogComponent } from './delete/espece-delete-dialog.component';
import { EspeceRoutingModule } from './route/espece-routing.module';

@NgModule({
  imports: [SharedModule, EspeceRoutingModule],
  declarations: [EspeceComponent, EspeceDetailComponent, EspeceUpdateComponent, EspeceDeleteDialogComponent],
  entryComponents: [EspeceDeleteDialogComponent],
})
export class ClassificationMsEspeceModule {}
