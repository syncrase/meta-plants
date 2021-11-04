import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAPGII, APGII } from '../apgii.model';
import { APGIIService } from '../service/apgii.service';

@Component({
  selector: 'gp-apgii-update',
  templateUrl: './apgii-update.component.html',
})
export class APGIIUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    ordre: [null, [Validators.required]],
    famille: [null, [Validators.required]],
  });

  constructor(protected aPGIIService: APGIIService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGII }) => {
      this.updateForm(aPGII);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aPGII = this.createFromForm();
    if (aPGII.id !== undefined) {
      this.subscribeToSaveResponse(this.aPGIIService.update(aPGII));
    } else {
      this.subscribeToSaveResponse(this.aPGIIService.create(aPGII));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAPGII>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(aPGII: IAPGII): void {
    this.editForm.patchValue({
      id: aPGII.id,
      ordre: aPGII.ordre,
      famille: aPGII.famille,
    });
  }

  protected createFromForm(): IAPGII {
    return {
      ...new APGII(),
      id: this.editForm.get(['id'])!.value,
      ordre: this.editForm.get(['ordre'])!.value,
      famille: this.editForm.get(['famille'])!.value,
    };
  }
}
