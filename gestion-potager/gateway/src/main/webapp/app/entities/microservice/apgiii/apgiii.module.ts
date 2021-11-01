import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { APGIIIComponent } from './apgiii.component';
import { APGIIIDetailComponent } from './apgiii-detail.component';
import { APGIIIUpdateComponent } from './apgiii-update.component';
import { APGIIIDeleteDialogComponent } from './apgiii-delete-dialog.component';
import { aPGIIIRoute } from './apgiii.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(aPGIIIRoute)],
  declarations: [APGIIIComponent, APGIIIDetailComponent, APGIIIUpdateComponent, APGIIIDeleteDialogComponent],
  entryComponents: [APGIIIDeleteDialogComponent],
})
export class MicroserviceAPGIIIModule {}
