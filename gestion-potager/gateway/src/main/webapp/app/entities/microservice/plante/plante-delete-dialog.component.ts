import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlante } from 'app/shared/model/microservice/plante.model';
import { PlanteService } from './plante.service';

@Component({
  templateUrl: './plante-delete-dialog.component.html',
})
export class PlanteDeleteDialogComponent {
  plante?: IPlante;

  constructor(protected planteService: PlanteService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.planteService.delete(id).subscribe(() => {
      this.eventManager.broadcast('planteListModification');
      this.activeModal.close();
    });
  }
}
