import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { APGIIComponent } from './list/apgii.component';
import { APGIIDetailComponent } from './detail/apgii-detail.component';
import { APGIIUpdateComponent } from './update/apgii-update.component';
import { APGIIDeleteDialogComponent } from './delete/apgii-delete-dialog.component';
import { APGIIRoutingModule } from './route/apgii-routing.module';

@NgModule({
  imports: [SharedModule, APGIIRoutingModule],
  declarations: [APGIIComponent, APGIIDetailComponent, APGIIUpdateComponent, APGIIDeleteDialogComponent],
  entryComponents: [APGIIDeleteDialogComponent],
})
export class PlantsmsAPGIIModule {}
