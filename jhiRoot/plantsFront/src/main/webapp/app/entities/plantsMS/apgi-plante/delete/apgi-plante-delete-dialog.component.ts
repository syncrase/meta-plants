import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAPGIPlante } from '../apgi-plante.model';
import { APGIPlanteService } from '../service/apgi-plante.service';

@Component({
  templateUrl: './apgi-plante-delete-dialog.component.html',
})
export class APGIPlanteDeleteDialogComponent {
  aPGIPlante?: IAPGIPlante;

  constructor(protected aPGIPlanteService: APGIPlanteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aPGIPlanteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
