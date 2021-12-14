import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISousGenre } from '../sous-genre.model';
import { SousGenreService } from '../service/sous-genre.service';

@Component({
  templateUrl: './sous-genre-delete-dialog.component.html',
})
export class SousGenreDeleteDialogComponent {
  sousGenre?: ISousGenre;

  constructor(protected sousGenreService: SousGenreService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sousGenreService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
