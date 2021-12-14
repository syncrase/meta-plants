import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InfraClasseComponent } from './list/infra-classe.component';
import { InfraClasseDetailComponent } from './detail/infra-classe-detail.component';
import { InfraClasseUpdateComponent } from './update/infra-classe-update.component';
import { InfraClasseDeleteDialogComponent } from './delete/infra-classe-delete-dialog.component';
import { InfraClasseRoutingModule } from './route/infra-classe-routing.module';

@NgModule({
  imports: [SharedModule, InfraClasseRoutingModule],
  declarations: [InfraClasseComponent, InfraClasseDetailComponent, InfraClasseUpdateComponent, InfraClasseDeleteDialogComponent],
  entryComponents: [InfraClasseDeleteDialogComponent],
})
export class ClassificationMsInfraClasseModule {}
