import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClassification } from 'app/shared/model/microservice/classification.model';
import { ClassificationService } from './classification.service';

@Component({
  templateUrl: './classification-delete-dialog.component.html',
})
export class ClassificationDeleteDialogComponent {
  classification?: IClassification;

  constructor(
    protected classificationService: ClassificationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classificationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('classificationListModification');
      this.activeModal.close();
    });
  }
}
