import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SuperFamilleComponent } from './list/super-famille.component';
import { SuperFamilleDetailComponent } from './detail/super-famille-detail.component';
import { SuperFamilleUpdateComponent } from './update/super-famille-update.component';
import { SuperFamilleDeleteDialogComponent } from './delete/super-famille-delete-dialog.component';
import { SuperFamilleRoutingModule } from './route/super-famille-routing.module';

@NgModule({
  imports: [SharedModule, SuperFamilleRoutingModule],
  declarations: [SuperFamilleComponent, SuperFamilleDetailComponent, SuperFamilleUpdateComponent, SuperFamilleDeleteDialogComponent],
  entryComponents: [SuperFamilleDeleteDialogComponent],
})
export class ClassificationMsSuperFamilleModule {}
