import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrdre } from '../ordre.model';
import { OrdreService } from '../service/ordre.service';

@Component({
  templateUrl: './ordre-delete-dialog.component.html',
})
export class OrdreDeleteDialogComponent {
  ordre?: IOrdre;

  constructor(protected ordreService: OrdreService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ordreService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
