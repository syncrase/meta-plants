import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CronquistComponent } from './list/cronquist.component';
import { CronquistDetailComponent } from './detail/cronquist-detail.component';
import { CronquistUpdateComponent } from './update/cronquist-update.component';
import { CronquistDeleteDialogComponent } from './delete/cronquist-delete-dialog.component';
import { CronquistRoutingModule } from './route/cronquist-routing.module';

@NgModule({
  imports: [SharedModule, CronquistRoutingModule],
  declarations: [CronquistComponent, CronquistDetailComponent, CronquistUpdateComponent, CronquistDeleteDialogComponent],
  entryComponents: [CronquistDeleteDialogComponent],
})
export class MicroserviceCronquistModule {}
