import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SousGenreComponent } from './list/sous-genre.component';
import { SousGenreDetailComponent } from './detail/sous-genre-detail.component';
import { SousGenreUpdateComponent } from './update/sous-genre-update.component';
import { SousGenreDeleteDialogComponent } from './delete/sous-genre-delete-dialog.component';
import { SousGenreRoutingModule } from './route/sous-genre-routing.module';

@NgModule({
  imports: [SharedModule, SousGenreRoutingModule],
  declarations: [SousGenreComponent, SousGenreDetailComponent, SousGenreUpdateComponent, SousGenreDeleteDialogComponent],
  entryComponents: [SousGenreDeleteDialogComponent],
})
export class ClassificationMsSousGenreModule {}
