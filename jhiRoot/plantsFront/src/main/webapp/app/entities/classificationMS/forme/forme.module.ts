import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FormeComponent } from './list/forme.component';
import { FormeDetailComponent } from './detail/forme-detail.component';
import { FormeUpdateComponent } from './update/forme-update.component';
import { FormeDeleteDialogComponent } from './delete/forme-delete-dialog.component';
import { FormeRoutingModule } from './route/forme-routing.module';

@NgModule({
  imports: [SharedModule, FormeRoutingModule],
  declarations: [FormeComponent, FormeDetailComponent, FormeUpdateComponent, FormeDeleteDialogComponent],
  entryComponents: [FormeDeleteDialogComponent],
})
export class ClassificationMsFormeModule {}
