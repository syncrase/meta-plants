import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAPGIV } from 'app/shared/model/microservice/apgiv.model';
import { APGIVService } from './apgiv.service';

@Component({
  templateUrl: './apgiv-delete-dialog.component.html',
})
export class APGIVDeleteDialogComponent {
  aPGIV?: IAPGIV;

  constructor(protected aPGIVService: APGIVService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aPGIVService.delete(id).subscribe(() => {
      this.eventManager.broadcast('aPGIVListModification');
      this.activeModal.close();
    });
  }
}
