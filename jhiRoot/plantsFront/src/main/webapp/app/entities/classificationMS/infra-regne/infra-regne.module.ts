import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InfraRegneComponent } from './list/infra-regne.component';
import { InfraRegneDetailComponent } from './detail/infra-regne-detail.component';
import { InfraRegneUpdateComponent } from './update/infra-regne-update.component';
import { InfraRegneDeleteDialogComponent } from './delete/infra-regne-delete-dialog.component';
import { InfraRegneRoutingModule } from './route/infra-regne-routing.module';

@NgModule({
  imports: [SharedModule, InfraRegneRoutingModule],
  declarations: [InfraRegneComponent, InfraRegneDetailComponent, InfraRegneUpdateComponent, InfraRegneDeleteDialogComponent],
  entryComponents: [InfraRegneDeleteDialogComponent],
})
export class ClassificationMsInfraRegneModule {}
