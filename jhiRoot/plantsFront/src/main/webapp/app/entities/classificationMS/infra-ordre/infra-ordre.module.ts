import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InfraOrdreComponent } from './list/infra-ordre.component';
import { InfraOrdreDetailComponent } from './detail/infra-ordre-detail.component';
import { InfraOrdreUpdateComponent } from './update/infra-ordre-update.component';
import { InfraOrdreDeleteDialogComponent } from './delete/infra-ordre-delete-dialog.component';
import { InfraOrdreRoutingModule } from './route/infra-ordre-routing.module';

@NgModule({
  imports: [SharedModule, InfraOrdreRoutingModule],
  declarations: [InfraOrdreComponent, InfraOrdreDetailComponent, InfraOrdreUpdateComponent, InfraOrdreDeleteDialogComponent],
  entryComponents: [InfraOrdreDeleteDialogComponent],
})
export class ClassificationMsInfraOrdreModule {}
