import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VarieteComponent } from './list/variete.component';
import { VarieteDetailComponent } from './detail/variete-detail.component';
import { VarieteUpdateComponent } from './update/variete-update.component';
import { VarieteDeleteDialogComponent } from './delete/variete-delete-dialog.component';
import { VarieteRoutingModule } from './route/variete-routing.module';

@NgModule({
  imports: [SharedModule, VarieteRoutingModule],
  declarations: [VarieteComponent, VarieteDetailComponent, VarieteUpdateComponent, VarieteDeleteDialogComponent],
  entryComponents: [VarieteDeleteDialogComponent],
})
export class ClassificationMsVarieteModule {}
