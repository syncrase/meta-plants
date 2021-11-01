import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAPGII } from 'app/shared/model/microservice/apgii.model';
import { APGIIService } from './apgii.service';

@Component({
  templateUrl: './apgii-delete-dialog.component.html',
})
export class APGIIDeleteDialogComponent {
  aPGII?: IAPGII;

  constructor(protected aPGIIService: APGIIService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aPGIIService.delete(id).subscribe(() => {
      this.eventManager.broadcast('aPGIIListModification');
      this.activeModal.close();
    });
  }
}
