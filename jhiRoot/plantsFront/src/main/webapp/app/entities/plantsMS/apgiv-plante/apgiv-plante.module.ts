import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { APGIVPlanteComponent } from './list/apgiv-plante.component';
import { APGIVPlanteDetailComponent } from './detail/apgiv-plante-detail.component';
import { APGIVPlanteUpdateComponent } from './update/apgiv-plante-update.component';
import { APGIVPlanteDeleteDialogComponent } from './delete/apgiv-plante-delete-dialog.component';
import { APGIVPlanteRoutingModule } from './route/apgiv-plante-routing.module';

@NgModule({
  imports: [SharedModule, APGIVPlanteRoutingModule],
  declarations: [APGIVPlanteComponent, APGIVPlanteDetailComponent, APGIVPlanteUpdateComponent, APGIVPlanteDeleteDialogComponent],
  entryComponents: [APGIVPlanteDeleteDialogComponent],
})
export class PlantsMsAPGIVPlanteModule {}
