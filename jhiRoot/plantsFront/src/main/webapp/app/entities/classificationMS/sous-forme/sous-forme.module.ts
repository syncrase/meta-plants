import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SousFormeComponent } from './list/sous-forme.component';
import { SousFormeDetailComponent } from './detail/sous-forme-detail.component';
import { SousFormeUpdateComponent } from './update/sous-forme-update.component';
import { SousFormeDeleteDialogComponent } from './delete/sous-forme-delete-dialog.component';
import { SousFormeRoutingModule } from './route/sous-forme-routing.module';

@NgModule({
  imports: [SharedModule, SousFormeRoutingModule],
  declarations: [SousFormeComponent, SousFormeDetailComponent, SousFormeUpdateComponent, SousFormeDeleteDialogComponent],
  entryComponents: [SousFormeDeleteDialogComponent],
})
export class ClassificationMsSousFormeModule {}
