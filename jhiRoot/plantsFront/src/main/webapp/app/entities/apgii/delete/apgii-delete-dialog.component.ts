import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAPGII } from '../apgii.model';
import { APGIIService } from '../service/apgii.service';

@Component({
  templateUrl: './apgii-delete-dialog.component.html',
})
export class APGIIDeleteDialogComponent {
  aPGII?: IAPGII;

  constructor(protected aPGIIService: APGIIService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aPGIIService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
