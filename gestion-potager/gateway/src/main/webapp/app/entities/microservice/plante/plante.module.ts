import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { PlanteComponent } from './plante.component';
import { PlanteDetailComponent } from './plante-detail.component';
import { PlanteUpdateComponent } from './plante-update.component';
import { PlanteDeleteDialogComponent } from './plante-delete-dialog.component';
import { planteRoute } from './plante.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(planteRoute)],
  declarations: [PlanteComponent, PlanteDetailComponent, PlanteUpdateComponent, PlanteDeleteDialogComponent],
  entryComponents: [PlanteDeleteDialogComponent],
})
export class MicroservicePlanteModule {}
