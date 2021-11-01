import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { APGIVComponent } from './apgiv.component';
import { APGIVDetailComponent } from './apgiv-detail.component';
import { APGIVUpdateComponent } from './apgiv-update.component';
import { APGIVDeleteDialogComponent } from './apgiv-delete-dialog.component';
import { aPGIVRoute } from './apgiv.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(aPGIVRoute)],
  declarations: [APGIVComponent, APGIVDetailComponent, APGIVUpdateComponent, APGIVDeleteDialogComponent],
  entryComponents: [APGIVDeleteDialogComponent],
})
export class MicroserviceAPGIVModule {}
