import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RaunkierComponent } from './list/raunkier.component';
import { RaunkierDetailComponent } from './detail/raunkier-detail.component';
import { RaunkierUpdateComponent } from './update/raunkier-update.component';
import { RaunkierDeleteDialogComponent } from './delete/raunkier-delete-dialog.component';
import { RaunkierRoutingModule } from './route/raunkier-routing.module';

@NgModule({
  imports: [SharedModule, RaunkierRoutingModule],
  declarations: [RaunkierComponent, RaunkierDetailComponent, RaunkierUpdateComponent, RaunkierDeleteDialogComponent],
  entryComponents: [RaunkierDeleteDialogComponent],
})
export class PlantsmsRaunkierModule {}
