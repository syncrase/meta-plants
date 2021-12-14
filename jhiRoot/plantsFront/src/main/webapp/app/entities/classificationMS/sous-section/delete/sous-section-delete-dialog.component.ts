import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISousSection } from '../sous-section.model';
import { SousSectionService } from '../service/sous-section.service';

@Component({
  templateUrl: './sous-section-delete-dialog.component.html',
})
export class SousSectionDeleteDialogComponent {
  sousSection?: ISousSection;

  constructor(protected sousSectionService: SousSectionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sousSectionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
