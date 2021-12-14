import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISuperOrdre } from '../super-ordre.model';
import { SuperOrdreService } from '../service/super-ordre.service';

@Component({
  templateUrl: './super-ordre-delete-dialog.component.html',
})
export class SuperOrdreDeleteDialogComponent {
  superOrdre?: ISuperOrdre;

  constructor(protected superOrdreService: SuperOrdreService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.superOrdreService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
