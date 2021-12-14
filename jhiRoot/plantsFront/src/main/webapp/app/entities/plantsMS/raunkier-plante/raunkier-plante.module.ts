import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RaunkierPlanteComponent } from './list/raunkier-plante.component';
import { RaunkierPlanteDetailComponent } from './detail/raunkier-plante-detail.component';
import { RaunkierPlanteUpdateComponent } from './update/raunkier-plante-update.component';
import { RaunkierPlanteDeleteDialogComponent } from './delete/raunkier-plante-delete-dialog.component';
import { RaunkierPlanteRoutingModule } from './route/raunkier-plante-routing.module';

@NgModule({
  imports: [SharedModule, RaunkierPlanteRoutingModule],
  declarations: [
    RaunkierPlanteComponent,
    RaunkierPlanteDetailComponent,
    RaunkierPlanteUpdateComponent,
    RaunkierPlanteDeleteDialogComponent,
  ],
  entryComponents: [RaunkierPlanteDeleteDialogComponent],
})
export class PlantsMsRaunkierPlanteModule {}
