import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SousRegneComponent } from './list/sous-regne.component';
import { SousRegneDetailComponent } from './detail/sous-regne-detail.component';
import { SousRegneUpdateComponent } from './update/sous-regne-update.component';
import { SousRegneDeleteDialogComponent } from './delete/sous-regne-delete-dialog.component';
import { SousRegneRoutingModule } from './route/sous-regne-routing.module';

@NgModule({
  imports: [SharedModule, SousRegneRoutingModule],
  declarations: [SousRegneComponent, SousRegneDetailComponent, SousRegneUpdateComponent, SousRegneDeleteDialogComponent],
  entryComponents: [SousRegneDeleteDialogComponent],
})
export class ClassificationMsSousRegneModule {}
