import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMicroEmbranchement } from '../micro-embranchement.model';
import { MicroEmbranchementService } from '../service/micro-embranchement.service';

@Component({
  templateUrl: './micro-embranchement-delete-dialog.component.html',
})
export class MicroEmbranchementDeleteDialogComponent {
  microEmbranchement?: IMicroEmbranchement;

  constructor(protected microEmbranchementService: MicroEmbranchementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.microEmbranchementService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
