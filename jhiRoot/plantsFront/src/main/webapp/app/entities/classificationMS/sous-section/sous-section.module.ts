import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SousSectionComponent } from './list/sous-section.component';
import { SousSectionDetailComponent } from './detail/sous-section-detail.component';
import { SousSectionUpdateComponent } from './update/sous-section-update.component';
import { SousSectionDeleteDialogComponent } from './delete/sous-section-delete-dialog.component';
import { SousSectionRoutingModule } from './route/sous-section-routing.module';

@NgModule({
  imports: [SharedModule, SousSectionRoutingModule],
  declarations: [SousSectionComponent, SousSectionDetailComponent, SousSectionUpdateComponent, SousSectionDeleteDialogComponent],
  entryComponents: [SousSectionDeleteDialogComponent],
})
export class ClassificationMsSousSectionModule {}
