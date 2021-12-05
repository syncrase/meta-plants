import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAPGIV } from '../apgiv.model';
import { APGIVService } from '../service/apgiv.service';

@Component({
  templateUrl: './apgiv-delete-dialog.component.html',
})
export class APGIVDeleteDialogComponent {
  aPGIV?: IAPGIV;

  constructor(protected aPGIVService: APGIVService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aPGIVService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
