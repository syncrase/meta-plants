import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { APGIVComponent } from './list/apgiv.component';
import { APGIVDetailComponent } from './detail/apgiv-detail.component';
import { APGIVUpdateComponent } from './update/apgiv-update.component';
import { APGIVDeleteDialogComponent } from './delete/apgiv-delete-dialog.component';
import { APGIVRoutingModule } from './route/apgiv-routing.module';

@NgModule({
  imports: [SharedModule, APGIVRoutingModule],
  declarations: [APGIVComponent, APGIVDetailComponent, APGIVUpdateComponent, APGIVDeleteDialogComponent],
  entryComponents: [APGIVDeleteDialogComponent],
})
export class APGIVModule {}
