import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVariete } from '../variete.model';
import { VarieteService } from '../service/variete.service';

@Component({
  templateUrl: './variete-delete-dialog.component.html',
})
export class VarieteDeleteDialogComponent {
  variete?: IVariete;

  constructor(protected varieteService: VarieteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.varieteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
