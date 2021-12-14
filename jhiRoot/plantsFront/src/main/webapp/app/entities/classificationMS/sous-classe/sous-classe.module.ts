import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SousClasseComponent } from './list/sous-classe.component';
import { SousClasseDetailComponent } from './detail/sous-classe-detail.component';
import { SousClasseUpdateComponent } from './update/sous-classe-update.component';
import { SousClasseDeleteDialogComponent } from './delete/sous-classe-delete-dialog.component';
import { SousClasseRoutingModule } from './route/sous-classe-routing.module';

@NgModule({
  imports: [SharedModule, SousClasseRoutingModule],
  declarations: [SousClasseComponent, SousClasseDetailComponent, SousClasseUpdateComponent, SousClasseDeleteDialogComponent],
  entryComponents: [SousClasseDeleteDialogComponent],
})
export class ClassificationMsSousClasseModule {}
