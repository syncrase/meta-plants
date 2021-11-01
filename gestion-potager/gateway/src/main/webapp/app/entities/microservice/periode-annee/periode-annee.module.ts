import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { PeriodeAnneeComponent } from './periode-annee.component';
import { PeriodeAnneeDetailComponent } from './periode-annee-detail.component';
import { PeriodeAnneeUpdateComponent } from './periode-annee-update.component';
import { PeriodeAnneeDeleteDialogComponent } from './periode-annee-delete-dialog.component';
import { periodeAnneeRoute } from './periode-annee.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(periodeAnneeRoute)],
  declarations: [PeriodeAnneeComponent, PeriodeAnneeDetailComponent, PeriodeAnneeUpdateComponent, PeriodeAnneeDeleteDialogComponent],
  entryComponents: [PeriodeAnneeDeleteDialogComponent],
})
export class MicroservicePeriodeAnneeModule {}
