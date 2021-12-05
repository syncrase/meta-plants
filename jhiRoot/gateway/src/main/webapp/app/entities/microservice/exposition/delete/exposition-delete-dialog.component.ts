import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExposition } from '../exposition.model';
import { ExpositionService } from '../service/exposition.service';

@Component({
  templateUrl: './exposition-delete-dialog.component.html',
})
export class ExpositionDeleteDialogComponent {
  exposition?: IExposition;

  constructor(protected expositionService: ExpositionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.expositionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
