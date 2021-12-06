import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { APGIIIComponent } from './list/apgiii.component';
import { APGIIIDetailComponent } from './detail/apgiii-detail.component';
import { APGIIIUpdateComponent } from './update/apgiii-update.component';
import { APGIIIDeleteDialogComponent } from './delete/apgiii-delete-dialog.component';
import { APGIIIRoutingModule } from './route/apgiii-routing.module';

@NgModule({
  imports: [SharedModule, APGIIIRoutingModule],
  declarations: [APGIIIComponent, APGIIIDetailComponent, APGIIIUpdateComponent, APGIIIDeleteDialogComponent],
  entryComponents: [APGIIIDeleteDialogComponent],
})
export class APGIIIModule {}
