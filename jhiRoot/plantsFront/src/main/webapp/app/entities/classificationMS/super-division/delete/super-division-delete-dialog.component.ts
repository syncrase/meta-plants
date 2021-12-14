import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISuperDivision } from '../super-division.model';
import { SuperDivisionService } from '../service/super-division.service';

@Component({
  templateUrl: './super-division-delete-dialog.component.html',
})
export class SuperDivisionDeleteDialogComponent {
  superDivision?: ISuperDivision;

  constructor(protected superDivisionService: SuperDivisionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.superDivisionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
