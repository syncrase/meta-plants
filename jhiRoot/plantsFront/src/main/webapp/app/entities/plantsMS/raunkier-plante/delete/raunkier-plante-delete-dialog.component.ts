import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRaunkierPlante } from '../raunkier-plante.model';
import { RaunkierPlanteService } from '../service/raunkier-plante.service';

@Component({
  templateUrl: './raunkier-plante-delete-dialog.component.html',
})
export class RaunkierPlanteDeleteDialogComponent {
  raunkierPlante?: IRaunkierPlante;

  constructor(protected raunkierPlanteService: RaunkierPlanteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.raunkierPlanteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
