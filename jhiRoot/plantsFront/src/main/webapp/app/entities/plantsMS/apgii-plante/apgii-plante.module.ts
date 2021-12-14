import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { APGIIPlanteComponent } from './list/apgii-plante.component';
import { APGIIPlanteDetailComponent } from './detail/apgii-plante-detail.component';
import { APGIIPlanteUpdateComponent } from './update/apgii-plante-update.component';
import { APGIIPlanteDeleteDialogComponent } from './delete/apgii-plante-delete-dialog.component';
import { APGIIPlanteRoutingModule } from './route/apgii-plante-routing.module';

@NgModule({
  imports: [SharedModule, APGIIPlanteRoutingModule],
  declarations: [APGIIPlanteComponent, APGIIPlanteDetailComponent, APGIIPlanteUpdateComponent, APGIIPlanteDeleteDialogComponent],
  entryComponents: [APGIIPlanteDeleteDialogComponent],
})
export class PlantsMsAPGIIPlanteModule {}
