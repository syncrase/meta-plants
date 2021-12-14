import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAPGIIPlante } from '../apgii-plante.model';
import { APGIIPlanteService } from '../service/apgii-plante.service';

@Component({
  templateUrl: './apgii-plante-delete-dialog.component.html',
})
export class APGIIPlanteDeleteDialogComponent {
  aPGIIPlante?: IAPGIIPlante;

  constructor(protected aPGIIPlanteService: APGIIPlanteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aPGIIPlanteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
