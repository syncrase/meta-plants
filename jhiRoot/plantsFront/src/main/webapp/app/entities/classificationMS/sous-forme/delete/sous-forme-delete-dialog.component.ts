import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISousForme } from '../sous-forme.model';
import { SousFormeService } from '../service/sous-forme.service';

@Component({
  templateUrl: './sous-forme-delete-dialog.component.html',
})
export class SousFormeDeleteDialogComponent {
  sousForme?: ISousForme;

  constructor(protected sousFormeService: SousFormeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sousFormeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
