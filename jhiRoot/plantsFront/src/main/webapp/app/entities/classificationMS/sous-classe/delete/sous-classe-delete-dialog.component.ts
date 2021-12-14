import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISousClasse } from '../sous-classe.model';
import { SousClasseService } from '../service/sous-classe.service';

@Component({
  templateUrl: './sous-classe-delete-dialog.component.html',
})
export class SousClasseDeleteDialogComponent {
  sousClasse?: ISousClasse;

  constructor(protected sousClasseService: SousClasseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sousClasseService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
