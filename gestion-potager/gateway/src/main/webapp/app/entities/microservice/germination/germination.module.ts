import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { GerminationComponent } from './germination.component';
import { GerminationDetailComponent } from './germination-detail.component';
import { GerminationUpdateComponent } from './germination-update.component';
import { GerminationDeleteDialogComponent } from './germination-delete-dialog.component';
import { germinationRoute } from './germination.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(germinationRoute)],
  declarations: [GerminationComponent, GerminationDetailComponent, GerminationUpdateComponent, GerminationDeleteDialogComponent],
  entryComponents: [GerminationDeleteDialogComponent],
})
export class MicroserviceGerminationModule {}
