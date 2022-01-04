import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassificationNom } from '../classification-nom.model';
import { ClassificationNomService } from '../service/classification-nom.service';

@Component({
  templateUrl: './classification-nom-delete-dialog.component.html',
})
export class ClassificationNomDeleteDialogComponent {
  classificationNom?: IClassificationNom;

  constructor(protected classificationNomService: ClassificationNomService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classificationNomService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
