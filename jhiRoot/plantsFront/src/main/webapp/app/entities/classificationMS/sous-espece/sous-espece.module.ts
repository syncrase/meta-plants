import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SousEspeceComponent } from './list/sous-espece.component';
import { SousEspeceDetailComponent } from './detail/sous-espece-detail.component';
import { SousEspeceUpdateComponent } from './update/sous-espece-update.component';
import { SousEspeceDeleteDialogComponent } from './delete/sous-espece-delete-dialog.component';
import { SousEspeceRoutingModule } from './route/sous-espece-routing.module';

@NgModule({
  imports: [SharedModule, SousEspeceRoutingModule],
  declarations: [SousEspeceComponent, SousEspeceDetailComponent, SousEspeceUpdateComponent, SousEspeceDeleteDialogComponent],
  entryComponents: [SousEspeceDeleteDialogComponent],
})
export class ClassificationMsSousEspeceModule {}
