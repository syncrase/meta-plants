import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { CronquistComponent } from './cronquist.component';
import { CronquistDetailComponent } from './cronquist-detail.component';
import { CronquistUpdateComponent } from './cronquist-update.component';
import { CronquistDeleteDialogComponent } from './cronquist-delete-dialog.component';
import { cronquistRoute } from './cronquist.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(cronquistRoute)],
  declarations: [CronquistComponent, CronquistDetailComponent, CronquistUpdateComponent, CronquistDeleteDialogComponent],
  entryComponents: [CronquistDeleteDialogComponent],
})
export class MicroserviceCronquistModule {}
