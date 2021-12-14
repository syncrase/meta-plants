import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RameauComponent } from './list/rameau.component';
import { RameauDetailComponent } from './detail/rameau-detail.component';
import { RameauUpdateComponent } from './update/rameau-update.component';
import { RameauDeleteDialogComponent } from './delete/rameau-delete-dialog.component';
import { RameauRoutingModule } from './route/rameau-routing.module';

@NgModule({
  imports: [SharedModule, RameauRoutingModule],
  declarations: [RameauComponent, RameauDetailComponent, RameauUpdateComponent, RameauDeleteDialogComponent],
  entryComponents: [RameauDeleteDialogComponent],
})
export class ClassificationMsRameauModule {}
