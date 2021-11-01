import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICronquist } from 'app/shared/model/microservice/cronquist.model';
import { CronquistService } from './cronquist.service';

@Component({
  templateUrl: './cronquist-delete-dialog.component.html',
})
export class CronquistDeleteDialogComponent {
  cronquist?: ICronquist;

  constructor(protected cronquistService: CronquistService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cronquistService.delete(id).subscribe(() => {
      this.eventManager.broadcast('cronquistListModification');
      this.activeModal.close();
    });
  }
}
