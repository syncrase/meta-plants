import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RegneComponent } from './list/regne.component';
import { RegneDetailComponent } from './detail/regne-detail.component';
import { RegneUpdateComponent } from './update/regne-update.component';
import { RegneDeleteDialogComponent } from './delete/regne-delete-dialog.component';
import { RegneRoutingModule } from './route/regne-routing.module';

@NgModule({
  imports: [SharedModule, RegneRoutingModule],
  declarations: [RegneComponent, RegneDetailComponent, RegneUpdateComponent, RegneDeleteDialogComponent],
  entryComponents: [RegneDeleteDialogComponent],
})
export class ClassificationMsRegneModule {}
