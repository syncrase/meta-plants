import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { APGIIComponent } from './apgii.component';
import { APGIIDetailComponent } from './apgii-detail.component';
import { APGIIUpdateComponent } from './apgii-update.component';
import { APGIIDeleteDialogComponent } from './apgii-delete-dialog.component';
import { aPGIIRoute } from './apgii.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(aPGIIRoute)],
  declarations: [APGIIComponent, APGIIDetailComponent, APGIIUpdateComponent, APGIIDeleteDialogComponent],
  entryComponents: [APGIIDeleteDialogComponent],
})
export class MicroserviceAPGIIModule {}
