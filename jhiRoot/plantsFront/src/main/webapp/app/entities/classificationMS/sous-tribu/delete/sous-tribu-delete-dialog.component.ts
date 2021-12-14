import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISousTribu } from '../sous-tribu.model';
import { SousTribuService } from '../service/sous-tribu.service';

@Component({
  templateUrl: './sous-tribu-delete-dialog.component.html',
})
export class SousTribuDeleteDialogComponent {
  sousTribu?: ISousTribu;

  constructor(protected sousTribuService: SousTribuService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sousTribuService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
