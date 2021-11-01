import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { CycleDeVieComponent } from './cycle-de-vie.component';
import { CycleDeVieDetailComponent } from './cycle-de-vie-detail.component';
import { CycleDeVieUpdateComponent } from './cycle-de-vie-update.component';
import { CycleDeVieDeleteDialogComponent } from './cycle-de-vie-delete-dialog.component';
import { cycleDeVieRoute } from './cycle-de-vie.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(cycleDeVieRoute)],
  declarations: [CycleDeVieComponent, CycleDeVieDetailComponent, CycleDeVieUpdateComponent, CycleDeVieDeleteDialogComponent],
  entryComponents: [CycleDeVieDeleteDialogComponent],
})
export class MicroserviceCycleDeVieModule {}
