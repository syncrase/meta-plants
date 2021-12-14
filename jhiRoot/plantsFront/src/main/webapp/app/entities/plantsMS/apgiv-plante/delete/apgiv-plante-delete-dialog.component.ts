import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAPGIVPlante } from '../apgiv-plante.model';
import { APGIVPlanteService } from '../service/apgiv-plante.service';

@Component({
  templateUrl: './apgiv-plante-delete-dialog.component.html',
})
export class APGIVPlanteDeleteDialogComponent {
  aPGIVPlante?: IAPGIVPlante;

  constructor(protected aPGIVPlanteService: APGIVPlanteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aPGIVPlanteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
