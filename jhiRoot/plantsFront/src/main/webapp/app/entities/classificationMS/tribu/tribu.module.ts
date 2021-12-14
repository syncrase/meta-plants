import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TribuComponent } from './list/tribu.component';
import { TribuDetailComponent } from './detail/tribu-detail.component';
import { TribuUpdateComponent } from './update/tribu-update.component';
import { TribuDeleteDialogComponent } from './delete/tribu-delete-dialog.component';
import { TribuRoutingModule } from './route/tribu-routing.module';

@NgModule({
  imports: [SharedModule, TribuRoutingModule],
  declarations: [TribuComponent, TribuDetailComponent, TribuUpdateComponent, TribuDeleteDialogComponent],
  entryComponents: [TribuDeleteDialogComponent],
})
export class ClassificationMsTribuModule {}
