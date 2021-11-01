import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { NomVernaculaireComponent } from './nom-vernaculaire.component';
import { NomVernaculaireDetailComponent } from './nom-vernaculaire-detail.component';
import { NomVernaculaireUpdateComponent } from './nom-vernaculaire-update.component';
import { NomVernaculaireDeleteDialogComponent } from './nom-vernaculaire-delete-dialog.component';
import { nomVernaculaireRoute } from './nom-vernaculaire.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(nomVernaculaireRoute)],
  declarations: [
    NomVernaculaireComponent,
    NomVernaculaireDetailComponent,
    NomVernaculaireUpdateComponent,
    NomVernaculaireDeleteDialogComponent,
  ],
  entryComponents: [NomVernaculaireDeleteDialogComponent],
})
export class MicroserviceNomVernaculaireModule {}
