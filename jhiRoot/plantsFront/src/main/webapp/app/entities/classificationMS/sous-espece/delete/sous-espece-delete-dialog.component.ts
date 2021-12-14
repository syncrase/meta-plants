import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISousEspece } from '../sous-espece.model';
import { SousEspeceService } from '../service/sous-espece.service';

@Component({
  templateUrl: './sous-espece-delete-dialog.component.html',
})
export class SousEspeceDeleteDialogComponent {
  sousEspece?: ISousEspece;

  constructor(protected sousEspeceService: SousEspeceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sousEspeceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
