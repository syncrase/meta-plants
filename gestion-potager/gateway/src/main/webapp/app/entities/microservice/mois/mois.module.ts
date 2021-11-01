import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { MoisComponent } from './mois.component';
import { MoisDetailComponent } from './mois-detail.component';
import { MoisUpdateComponent } from './mois-update.component';
import { MoisDeleteDialogComponent } from './mois-delete-dialog.component';
import { moisRoute } from './mois.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(moisRoute)],
  declarations: [MoisComponent, MoisDetailComponent, MoisUpdateComponent, MoisDeleteDialogComponent],
  entryComponents: [MoisDeleteDialogComponent],
})
export class MicroserviceMoisModule {}
