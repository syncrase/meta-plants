import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISuperFamille } from '../super-famille.model';
import { SuperFamilleService } from '../service/super-famille.service';

@Component({
  templateUrl: './super-famille-delete-dialog.component.html',
})
export class SuperFamilleDeleteDialogComponent {
  superFamille?: ISuperFamille;

  constructor(protected superFamilleService: SuperFamilleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.superFamilleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
