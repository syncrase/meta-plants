import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { APGIComponent } from './list/apgi.component';
import { APGIDetailComponent } from './detail/apgi-detail.component';
import { APGIUpdateComponent } from './update/apgi-update.component';
import { APGIDeleteDialogComponent } from './delete/apgi-delete-dialog.component';
import { APGIRoutingModule } from './route/apgi-routing.module';

@NgModule({
  imports: [SharedModule, APGIRoutingModule],
  declarations: [APGIComponent, APGIDetailComponent, APGIUpdateComponent, APGIDeleteDialogComponent],
  entryComponents: [APGIDeleteDialogComponent],
})
export class PlantsmsAPGIModule {}
