import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAPGI } from '../apgi.model';
import { APGIService } from '../service/apgi.service';

@Component({
  templateUrl: './apgi-delete-dialog.component.html',
})
export class APGIDeleteDialogComponent {
  aPGI?: IAPGI;

  constructor(protected aPGIService: APGIService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aPGIService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
