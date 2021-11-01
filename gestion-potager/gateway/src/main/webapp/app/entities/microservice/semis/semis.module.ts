import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { SemisComponent } from './semis.component';
import { SemisDetailComponent } from './semis-detail.component';
import { SemisUpdateComponent } from './semis-update.component';
import { SemisDeleteDialogComponent } from './semis-delete-dialog.component';
import { semisRoute } from './semis.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(semisRoute)],
  declarations: [SemisComponent, SemisDetailComponent, SemisUpdateComponent, SemisDeleteDialogComponent],
  entryComponents: [SemisDeleteDialogComponent],
})
export class MicroserviceSemisModule {}
