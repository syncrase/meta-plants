import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInfraEmbranchement } from '../infra-embranchement.model';
import { InfraEmbranchementService } from '../service/infra-embranchement.service';

@Component({
  templateUrl: './infra-embranchement-delete-dialog.component.html',
})
export class InfraEmbranchementDeleteDialogComponent {
  infraEmbranchement?: IInfraEmbranchement;

  constructor(protected infraEmbranchementService: InfraEmbranchementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.infraEmbranchementService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
