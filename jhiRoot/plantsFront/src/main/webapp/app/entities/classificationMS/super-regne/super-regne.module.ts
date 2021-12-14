import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SuperRegneComponent } from './list/super-regne.component';
import { SuperRegneDetailComponent } from './detail/super-regne-detail.component';
import { SuperRegneUpdateComponent } from './update/super-regne-update.component';
import { SuperRegneDeleteDialogComponent } from './delete/super-regne-delete-dialog.component';
import { SuperRegneRoutingModule } from './route/super-regne-routing.module';

@NgModule({
  imports: [SharedModule, SuperRegneRoutingModule],
  declarations: [SuperRegneComponent, SuperRegneDetailComponent, SuperRegneUpdateComponent, SuperRegneDeleteDialogComponent],
  entryComponents: [SuperRegneDeleteDialogComponent],
})
export class ClassificationMsSuperRegneModule {}
