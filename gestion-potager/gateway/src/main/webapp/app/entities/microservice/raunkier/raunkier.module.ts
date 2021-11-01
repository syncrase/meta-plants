import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { RaunkierComponent } from './raunkier.component';
import { RaunkierDetailComponent } from './raunkier-detail.component';
import { RaunkierUpdateComponent } from './raunkier-update.component';
import { RaunkierDeleteDialogComponent } from './raunkier-delete-dialog.component';
import { raunkierRoute } from './raunkier.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(raunkierRoute)],
  declarations: [RaunkierComponent, RaunkierDetailComponent, RaunkierUpdateComponent, RaunkierDeleteDialogComponent],
  entryComponents: [RaunkierDeleteDialogComponent],
})
export class MicroserviceRaunkierModule {}
