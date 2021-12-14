import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SuperClasseComponent } from './list/super-classe.component';
import { SuperClasseDetailComponent } from './detail/super-classe-detail.component';
import { SuperClasseUpdateComponent } from './update/super-classe-update.component';
import { SuperClasseDeleteDialogComponent } from './delete/super-classe-delete-dialog.component';
import { SuperClasseRoutingModule } from './route/super-classe-routing.module';

@NgModule({
  imports: [SharedModule, SuperClasseRoutingModule],
  declarations: [SuperClasseComponent, SuperClasseDetailComponent, SuperClasseUpdateComponent, SuperClasseDeleteDialogComponent],
  entryComponents: [SuperClasseDeleteDialogComponent],
})
export class ClassificationMsSuperClasseModule {}
