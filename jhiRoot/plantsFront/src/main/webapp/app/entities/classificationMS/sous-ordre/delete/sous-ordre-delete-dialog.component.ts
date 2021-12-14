import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISousOrdre } from '../sous-ordre.model';
import { SousOrdreService } from '../service/sous-ordre.service';

@Component({
  templateUrl: './sous-ordre-delete-dialog.component.html',
})
export class SousOrdreDeleteDialogComponent {
  sousOrdre?: ISousOrdre;

  constructor(protected sousOrdreService: SousOrdreService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sousOrdreService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
