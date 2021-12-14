import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInfraClasse } from '../infra-classe.model';
import { InfraClasseService } from '../service/infra-classe.service';

@Component({
  templateUrl: './infra-classe-delete-dialog.component.html',
})
export class InfraClasseDeleteDialogComponent {
  infraClasse?: IInfraClasse;

  constructor(protected infraClasseService: InfraClasseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.infraClasseService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
