import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAPGIIIPlante } from '../apgiii-plante.model';
import { APGIIIPlanteService } from '../service/apgiii-plante.service';

@Component({
  templateUrl: './apgiii-plante-delete-dialog.component.html',
})
export class APGIIIPlanteDeleteDialogComponent {
  aPGIIIPlante?: IAPGIIIPlante;

  constructor(protected aPGIIIPlanteService: APGIIIPlanteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aPGIIIPlanteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
