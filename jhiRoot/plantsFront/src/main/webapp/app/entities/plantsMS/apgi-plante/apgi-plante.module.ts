import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { APGIPlanteComponent } from './list/apgi-plante.component';
import { APGIPlanteDetailComponent } from './detail/apgi-plante-detail.component';
import { APGIPlanteUpdateComponent } from './update/apgi-plante-update.component';
import { APGIPlanteDeleteDialogComponent } from './delete/apgi-plante-delete-dialog.component';
import { APGIPlanteRoutingModule } from './route/apgi-plante-routing.module';

@NgModule({
  imports: [SharedModule, APGIPlanteRoutingModule],
  declarations: [APGIPlanteComponent, APGIPlanteDetailComponent, APGIPlanteUpdateComponent, APGIPlanteDeleteDialogComponent],
  entryComponents: [APGIPlanteDeleteDialogComponent],
})
export class PlantsMsAPGIPlanteModule {}
