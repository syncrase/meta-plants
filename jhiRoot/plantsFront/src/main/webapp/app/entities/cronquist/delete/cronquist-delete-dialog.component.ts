import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICronquist } from '../cronquist.model';
import { CronquistService } from '../service/cronquist.service';

@Component({
  templateUrl: './cronquist-delete-dialog.component.html',
})
export class CronquistDeleteDialogComponent {
  cronquist?: ICronquist;

  constructor(protected cronquistService: CronquistService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cronquistService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
