import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISuperClasse } from '../super-classe.model';
import { SuperClasseService } from '../service/super-classe.service';

@Component({
  templateUrl: './super-classe-delete-dialog.component.html',
})
export class SuperClasseDeleteDialogComponent {
  superClasse?: ISuperClasse;

  constructor(protected superClasseService: SuperClasseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.superClasseService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
