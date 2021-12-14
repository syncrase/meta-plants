import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInfraOrdre } from '../infra-ordre.model';
import { InfraOrdreService } from '../service/infra-ordre.service';

@Component({
  templateUrl: './infra-ordre-delete-dialog.component.html',
})
export class InfraOrdreDeleteDialogComponent {
  infraOrdre?: IInfraOrdre;

  constructor(protected infraOrdreService: InfraOrdreService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.infraOrdreService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
