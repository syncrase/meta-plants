import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISousRegne } from '../sous-regne.model';
import { SousRegneService } from '../service/sous-regne.service';

@Component({
  templateUrl: './sous-regne-delete-dialog.component.html',
})
export class SousRegneDeleteDialogComponent {
  sousRegne?: ISousRegne;

  constructor(protected sousRegneService: SousRegneService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sousRegneService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
