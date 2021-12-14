import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISuperRegne } from '../super-regne.model';
import { SuperRegneService } from '../service/super-regne.service';

@Component({
  templateUrl: './super-regne-delete-dialog.component.html',
})
export class SuperRegneDeleteDialogComponent {
  superRegne?: ISuperRegne;

  constructor(protected superRegneService: SuperRegneService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.superRegneService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
