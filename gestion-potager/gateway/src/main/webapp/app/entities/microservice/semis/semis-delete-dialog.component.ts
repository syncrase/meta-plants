import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISemis } from 'app/shared/model/microservice/semis.model';
import { SemisService } from './semis.service';

@Component({
  templateUrl: './semis-delete-dialog.component.html',
})
export class SemisDeleteDialogComponent {
  semis?: ISemis;

  constructor(protected semisService: SemisService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.semisService.delete(id).subscribe(() => {
      this.eventManager.broadcast('semisListModification');
      this.activeModal.close();
    });
  }
}
