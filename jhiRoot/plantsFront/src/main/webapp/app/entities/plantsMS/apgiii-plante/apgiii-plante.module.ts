import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { APGIIIPlanteComponent } from './list/apgiii-plante.component';
import { APGIIIPlanteDetailComponent } from './detail/apgiii-plante-detail.component';
import { APGIIIPlanteUpdateComponent } from './update/apgiii-plante-update.component';
import { APGIIIPlanteDeleteDialogComponent } from './delete/apgiii-plante-delete-dialog.component';
import { APGIIIPlanteRoutingModule } from './route/apgiii-plante-routing.module';

@NgModule({
  imports: [SharedModule, APGIIIPlanteRoutingModule],
  declarations: [APGIIIPlanteComponent, APGIIIPlanteDetailComponent, APGIIIPlanteUpdateComponent, APGIIIPlanteDeleteDialogComponent],
  entryComponents: [APGIIIPlanteDeleteDialogComponent],
})
export class PlantsMsAPGIIIPlanteModule {}
