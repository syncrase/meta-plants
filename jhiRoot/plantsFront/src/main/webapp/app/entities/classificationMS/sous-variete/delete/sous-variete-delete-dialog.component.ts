import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISousVariete } from '../sous-variete.model';
import { SousVarieteService } from '../service/sous-variete.service';

@Component({
  templateUrl: './sous-variete-delete-dialog.component.html',
})
export class SousVarieteDeleteDialogComponent {
  sousVariete?: ISousVariete;

  constructor(protected sousVarieteService: SousVarieteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sousVarieteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
