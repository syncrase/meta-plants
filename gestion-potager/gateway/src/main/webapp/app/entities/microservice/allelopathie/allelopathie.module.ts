import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { AllelopathieComponent } from './allelopathie.component';
import { AllelopathieDetailComponent } from './allelopathie-detail.component';
import { AllelopathieUpdateComponent } from './allelopathie-update.component';
import { AllelopathieDeleteDialogComponent } from './allelopathie-delete-dialog.component';
import { allelopathieRoute } from './allelopathie.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(allelopathieRoute)],
  declarations: [AllelopathieComponent, AllelopathieDetailComponent, AllelopathieUpdateComponent, AllelopathieDeleteDialogComponent],
  entryComponents: [AllelopathieDeleteDialogComponent],
})
export class MicroserviceAllelopathieModule {}
