import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CronquistPlanteComponent } from './list/cronquist-plante.component';
import { CronquistPlanteDetailComponent } from './detail/cronquist-plante-detail.component';
import { CronquistPlanteUpdateComponent } from './update/cronquist-plante-update.component';
import { CronquistPlanteDeleteDialogComponent } from './delete/cronquist-plante-delete-dialog.component';
import { CronquistPlanteRoutingModule } from './route/cronquist-plante-routing.module';

@NgModule({
  imports: [SharedModule, CronquistPlanteRoutingModule],
  declarations: [
    CronquistPlanteComponent,
    CronquistPlanteDetailComponent,
    CronquistPlanteUpdateComponent,
    CronquistPlanteDeleteDialogComponent,
  ],
  entryComponents: [CronquistPlanteDeleteDialogComponent],
})
export class PlantsMsCronquistPlanteModule {}
