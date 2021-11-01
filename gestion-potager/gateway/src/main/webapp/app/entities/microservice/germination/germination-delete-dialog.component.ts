import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGermination } from 'app/shared/model/microservice/germination.model';
import { GerminationService } from './germination.service';

@Component({
  templateUrl: './germination-delete-dialog.component.html',
})
export class GerminationDeleteDialogComponent {
  germination?: IGermination;

  constructor(
    protected germinationService: GerminationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.germinationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('germinationListModification');
      this.activeModal.close();
    });
  }
}
