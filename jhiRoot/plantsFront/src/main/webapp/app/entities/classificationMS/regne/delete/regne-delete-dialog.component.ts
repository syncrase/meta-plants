import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRegne } from '../regne.model';
import { RegneService } from '../service/regne.service';

@Component({
  templateUrl: './regne-delete-dialog.component.html',
})
export class RegneDeleteDialogComponent {
  regne?: IRegne;

  constructor(protected regneService: RegneService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.regneService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
