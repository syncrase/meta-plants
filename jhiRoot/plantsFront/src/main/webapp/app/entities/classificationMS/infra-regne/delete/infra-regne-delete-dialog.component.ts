import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInfraRegne } from '../infra-regne.model';
import { InfraRegneService } from '../service/infra-regne.service';

@Component({
  templateUrl: './infra-regne-delete-dialog.component.html',
})
export class InfraRegneDeleteDialogComponent {
  infraRegne?: IInfraRegne;

  constructor(protected infraRegneService: InfraRegneService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.infraRegneService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
