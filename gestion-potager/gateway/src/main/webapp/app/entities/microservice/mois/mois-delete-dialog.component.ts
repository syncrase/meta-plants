import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMois } from 'app/shared/model/microservice/mois.model';
import { MoisService } from './mois.service';

@Component({
  templateUrl: './mois-delete-dialog.component.html',
})
export class MoisDeleteDialogComponent {
  mois?: IMois;

  constructor(protected moisService: MoisService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.moisService.delete(id).subscribe(() => {
      this.eventManager.broadcast('moisListModification');
      this.activeModal.close();
    });
  }
}
