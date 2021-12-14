import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRameau } from '../rameau.model';
import { RameauService } from '../service/rameau.service';

@Component({
  templateUrl: './rameau-delete-dialog.component.html',
})
export class RameauDeleteDialogComponent {
  rameau?: IRameau;

  constructor(protected rameauService: RameauService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rameauService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
