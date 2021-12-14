import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMicroOrdre } from '../micro-ordre.model';
import { MicroOrdreService } from '../service/micro-ordre.service';

@Component({
  templateUrl: './micro-ordre-delete-dialog.component.html',
})
export class MicroOrdreDeleteDialogComponent {
  microOrdre?: IMicroOrdre;

  constructor(protected microOrdreService: MicroOrdreService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.microOrdreService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
