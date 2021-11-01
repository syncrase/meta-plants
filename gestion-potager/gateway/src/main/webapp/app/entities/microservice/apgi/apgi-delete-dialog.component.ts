import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAPGI } from 'app/shared/model/microservice/apgi.model';
import { APGIService } from './apgi.service';

@Component({
  templateUrl: './apgi-delete-dialog.component.html',
})
export class APGIDeleteDialogComponent {
  aPGI?: IAPGI;

  constructor(protected aPGIService: APGIService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aPGIService.delete(id).subscribe(() => {
      this.eventManager.broadcast('aPGIListModification');
      this.activeModal.close();
    });
  }
}
