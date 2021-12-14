import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InfraEmbranchementComponent } from './list/infra-embranchement.component';
import { InfraEmbranchementDetailComponent } from './detail/infra-embranchement-detail.component';
import { InfraEmbranchementUpdateComponent } from './update/infra-embranchement-update.component';
import { InfraEmbranchementDeleteDialogComponent } from './delete/infra-embranchement-delete-dialog.component';
import { InfraEmbranchementRoutingModule } from './route/infra-embranchement-routing.module';

@NgModule({
  imports: [SharedModule, InfraEmbranchementRoutingModule],
  declarations: [
    InfraEmbranchementComponent,
    InfraEmbranchementDetailComponent,
    InfraEmbranchementUpdateComponent,
    InfraEmbranchementDeleteDialogComponent,
  ],
  entryComponents: [InfraEmbranchementDeleteDialogComponent],
})
export class ClassificationMsInfraEmbranchementModule {}
