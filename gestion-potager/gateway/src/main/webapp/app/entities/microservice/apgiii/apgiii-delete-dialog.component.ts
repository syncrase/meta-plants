import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAPGIII } from 'app/shared/model/microservice/apgiii.model';
import { APGIIIService } from './apgiii.service';

@Component({
  templateUrl: './apgiii-delete-dialog.component.html',
})
export class APGIIIDeleteDialogComponent {
  aPGIII?: IAPGIII;

  constructor(protected aPGIIIService: APGIIIService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aPGIIIService.delete(id).subscribe(() => {
      this.eventManager.broadcast('aPGIIIListModification');
      this.activeModal.close();
    });
  }
}
