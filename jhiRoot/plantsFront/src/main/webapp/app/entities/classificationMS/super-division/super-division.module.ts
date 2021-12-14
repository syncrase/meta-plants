import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SuperDivisionComponent } from './list/super-division.component';
import { SuperDivisionDetailComponent } from './detail/super-division-detail.component';
import { SuperDivisionUpdateComponent } from './update/super-division-update.component';
import { SuperDivisionDeleteDialogComponent } from './delete/super-division-delete-dialog.component';
import { SuperDivisionRoutingModule } from './route/super-division-routing.module';

@NgModule({
  imports: [SharedModule, SuperDivisionRoutingModule],
  declarations: [SuperDivisionComponent, SuperDivisionDetailComponent, SuperDivisionUpdateComponent, SuperDivisionDeleteDialogComponent],
  entryComponents: [SuperDivisionDeleteDialogComponent],
})
export class ClassificationMsSuperDivisionModule {}
