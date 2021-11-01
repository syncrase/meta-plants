import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { RessemblanceComponent } from './ressemblance.component';
import { RessemblanceDetailComponent } from './ressemblance-detail.component';
import { RessemblanceUpdateComponent } from './ressemblance-update.component';
import { RessemblanceDeleteDialogComponent } from './ressemblance-delete-dialog.component';
import { ressemblanceRoute } from './ressemblance.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(ressemblanceRoute)],
  declarations: [RessemblanceComponent, RessemblanceDetailComponent, RessemblanceUpdateComponent, RessemblanceDeleteDialogComponent],
  entryComponents: [RessemblanceDeleteDialogComponent],
})
export class MicroserviceRessemblanceModule {}
