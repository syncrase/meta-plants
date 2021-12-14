import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClade } from '../clade.model';
import { CladeService } from '../service/clade.service';

@Component({
  templateUrl: './clade-delete-dialog.component.html',
})
export class CladeDeleteDialogComponent {
  clade?: IClade;

  constructor(protected cladeService: CladeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cladeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
