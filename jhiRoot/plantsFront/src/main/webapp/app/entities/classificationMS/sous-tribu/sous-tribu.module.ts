import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SousTribuComponent } from './list/sous-tribu.component';
import { SousTribuDetailComponent } from './detail/sous-tribu-detail.component';
import { SousTribuUpdateComponent } from './update/sous-tribu-update.component';
import { SousTribuDeleteDialogComponent } from './delete/sous-tribu-delete-dialog.component';
import { SousTribuRoutingModule } from './route/sous-tribu-routing.module';

@NgModule({
  imports: [SharedModule, SousTribuRoutingModule],
  declarations: [SousTribuComponent, SousTribuDetailComponent, SousTribuUpdateComponent, SousTribuDeleteDialogComponent],
  entryComponents: [SousTribuDeleteDialogComponent],
})
export class ClassificationMsSousTribuModule {}
