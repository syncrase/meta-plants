import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassificationCronquist } from '../classification-cronquist.model';
import { ClassificationCronquistService } from '../service/classification-cronquist.service';

@Component({
  templateUrl: './classification-cronquist-delete-dialog.component.html',
})
export class ClassificationCronquistDeleteDialogComponent {
  classificationCronquist?: IClassificationCronquist;

  constructor(protected classificationCronquistService: ClassificationCronquistService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classificationCronquistService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
