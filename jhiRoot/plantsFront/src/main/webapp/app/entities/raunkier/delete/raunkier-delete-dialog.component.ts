import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRaunkier } from '../raunkier.model';
import { RaunkierService } from '../service/raunkier.service';

@Component({
  templateUrl: './raunkier-delete-dialog.component.html',
})
export class RaunkierDeleteDialogComponent {
  raunkier?: IRaunkier;

  constructor(protected raunkierService: RaunkierService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.raunkierService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
