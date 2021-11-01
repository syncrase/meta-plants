import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISol } from 'app/shared/model/microservice/sol.model';
import { SolService } from './sol.service';

@Component({
  templateUrl: './sol-delete-dialog.component.html',
})
export class SolDeleteDialogComponent {
  sol?: ISol;

  constructor(protected solService: SolService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.solService.delete(id).subscribe(() => {
      this.eventManager.broadcast('solListModification');
      this.activeModal.close();
    });
  }
}
