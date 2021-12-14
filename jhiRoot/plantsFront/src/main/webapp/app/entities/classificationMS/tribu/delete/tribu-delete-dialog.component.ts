import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITribu } from '../tribu.model';
import { TribuService } from '../service/tribu.service';

@Component({
  templateUrl: './tribu-delete-dialog.component.html',
})
export class TribuDeleteDialogComponent {
  tribu?: ITribu;

  constructor(protected tribuService: TribuService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tribuService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
