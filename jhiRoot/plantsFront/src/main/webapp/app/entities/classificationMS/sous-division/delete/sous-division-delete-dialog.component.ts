import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISousDivision } from '../sous-division.model';
import { SousDivisionService } from '../service/sous-division.service';

@Component({
  templateUrl: './sous-division-delete-dialog.component.html',
})
export class SousDivisionDeleteDialogComponent {
  sousDivision?: ISousDivision;

  constructor(protected sousDivisionService: SousDivisionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sousDivisionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
