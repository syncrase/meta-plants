import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CladeComponent } from './list/clade.component';
import { CladeDetailComponent } from './detail/clade-detail.component';
import { CladeUpdateComponent } from './update/clade-update.component';
import { CladeDeleteDialogComponent } from './delete/clade-delete-dialog.component';
import { CladeRoutingModule } from './route/clade-routing.module';

@NgModule({
  imports: [SharedModule, CladeRoutingModule],
  declarations: [CladeComponent, CladeDetailComponent, CladeUpdateComponent, CladeDeleteDialogComponent],
  entryComponents: [CladeDeleteDialogComponent],
})
export class PlantsMsCladeModule {}
