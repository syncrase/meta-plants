import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEspece } from '../espece.model';
import { EspeceService } from '../service/espece.service';

@Component({
  templateUrl: './espece-delete-dialog.component.html',
})
export class EspeceDeleteDialogComponent {
  espece?: IEspece;

  constructor(protected especeService: EspeceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.especeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
