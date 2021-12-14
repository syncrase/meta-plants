import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SousVarieteComponent } from './list/sous-variete.component';
import { SousVarieteDetailComponent } from './detail/sous-variete-detail.component';
import { SousVarieteUpdateComponent } from './update/sous-variete-update.component';
import { SousVarieteDeleteDialogComponent } from './delete/sous-variete-delete-dialog.component';
import { SousVarieteRoutingModule } from './route/sous-variete-routing.module';

@NgModule({
  imports: [SharedModule, SousVarieteRoutingModule],
  declarations: [SousVarieteComponent, SousVarieteDetailComponent, SousVarieteUpdateComponent, SousVarieteDeleteDialogComponent],
  entryComponents: [SousVarieteDeleteDialogComponent],
})
export class ClassificationMsSousVarieteModule {}
