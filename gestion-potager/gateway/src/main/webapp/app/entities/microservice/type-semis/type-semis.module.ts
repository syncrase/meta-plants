import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { TypeSemisComponent } from './type-semis.component';
import { TypeSemisDetailComponent } from './type-semis-detail.component';
import { TypeSemisUpdateComponent } from './type-semis-update.component';
import { TypeSemisDeleteDialogComponent } from './type-semis-delete-dialog.component';
import { typeSemisRoute } from './type-semis.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(typeSemisRoute)],
  declarations: [TypeSemisComponent, TypeSemisDetailComponent, TypeSemisUpdateComponent, TypeSemisDeleteDialogComponent],
  entryComponents: [TypeSemisDeleteDialogComponent],
})
export class MicroserviceTypeSemisModule {}
