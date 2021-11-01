import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { APGIComponent } from './apgi.component';
import { APGIDetailComponent } from './apgi-detail.component';
import { APGIUpdateComponent } from './apgi-update.component';
import { APGIDeleteDialogComponent } from './apgi-delete-dialog.component';
import { aPGIRoute } from './apgi.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(aPGIRoute)],
  declarations: [APGIComponent, APGIDetailComponent, APGIUpdateComponent, APGIDeleteDialogComponent],
  entryComponents: [APGIDeleteDialogComponent],
})
export class MicroserviceAPGIModule {}
