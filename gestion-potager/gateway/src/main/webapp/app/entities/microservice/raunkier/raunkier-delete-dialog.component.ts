import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRaunkier } from 'app/shared/model/microservice/raunkier.model';
import { RaunkierService } from './raunkier.service';

@Component({
  templateUrl: './raunkier-delete-dialog.component.html',
})
export class RaunkierDeleteDialogComponent {
  raunkier?: IRaunkier;

  constructor(protected raunkierService: RaunkierService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.raunkierService.delete(id).subscribe(() => {
      this.eventManager.broadcast('raunkierListModification');
      this.activeModal.close();
    });
  }
}
