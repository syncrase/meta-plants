import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { SolComponent } from './sol.component';
import { SolDetailComponent } from './sol-detail.component';
import { SolUpdateComponent } from './sol-update.component';
import { SolDeleteDialogComponent } from './sol-delete-dialog.component';
import { solRoute } from './sol.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(solRoute)],
  declarations: [SolComponent, SolDetailComponent, SolUpdateComponent, SolDeleteDialogComponent],
  entryComponents: [SolDeleteDialogComponent],
})
export class MicroserviceSolModule {}
