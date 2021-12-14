import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SousDivisionComponent } from './list/sous-division.component';
import { SousDivisionDetailComponent } from './detail/sous-division-detail.component';
import { SousDivisionUpdateComponent } from './update/sous-division-update.component';
import { SousDivisionDeleteDialogComponent } from './delete/sous-division-delete-dialog.component';
import { SousDivisionRoutingModule } from './route/sous-division-routing.module';

@NgModule({
  imports: [SharedModule, SousDivisionRoutingModule],
  declarations: [SousDivisionComponent, SousDivisionDetailComponent, SousDivisionUpdateComponent, SousDivisionDeleteDialogComponent],
  entryComponents: [SousDivisionDeleteDialogComponent],
})
export class ClassificationMsSousDivisionModule {}
