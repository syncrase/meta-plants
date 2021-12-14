import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICronquistPlante } from '../cronquist-plante.model';
import { CronquistPlanteService } from '../service/cronquist-plante.service';

@Component({
  templateUrl: './cronquist-plante-delete-dialog.component.html',
})
export class CronquistPlanteDeleteDialogComponent {
  cronquistPlante?: ICronquistPlante;

  constructor(protected cronquistPlanteService: CronquistPlanteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cronquistPlanteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
