import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MicroEmbranchementComponent } from './list/micro-embranchement.component';
import { MicroEmbranchementDetailComponent } from './detail/micro-embranchement-detail.component';
import { MicroEmbranchementUpdateComponent } from './update/micro-embranchement-update.component';
import { MicroEmbranchementDeleteDialogComponent } from './delete/micro-embranchement-delete-dialog.component';
import { MicroEmbranchementRoutingModule } from './route/micro-embranchement-routing.module';

@NgModule({
  imports: [SharedModule, MicroEmbranchementRoutingModule],
  declarations: [
    MicroEmbranchementComponent,
    MicroEmbranchementDetailComponent,
    MicroEmbranchementUpdateComponent,
    MicroEmbranchementDeleteDialogComponent,
  ],
  entryComponents: [MicroEmbranchementDeleteDialogComponent],
})
export class ClassificationMsMicroEmbranchementModule {}
