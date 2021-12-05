import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAPGIII } from '../apgiii.model';
import { APGIIIService } from '../service/apgiii.service';

@Component({
  templateUrl: './apgiii-delete-dialog.component.html',
})
export class APGIIIDeleteDialogComponent {
  aPGIII?: IAPGIII;

  constructor(protected aPGIIIService: APGIIIService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aPGIIIService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
