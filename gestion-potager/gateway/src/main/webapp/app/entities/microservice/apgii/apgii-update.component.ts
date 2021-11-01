import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAPGII, APGII } from 'app/shared/model/microservice/apgii.model';
import { APGIIService } from './apgii.service';

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

  constructor(protected aPGIIService: APGIIService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGII }) => {
      this.updateForm(aPGII);
    });
  }

  updateForm(aPGII: IAPGII): void {
    this.editForm.patchValue({
      id: aPGII.id,
      ordre: aPGII.ordre,
      famille: aPGII.famille,
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

  private createFromForm(): IAPGII {
    return {
      ...new APGII(),
      id: this.editForm.get(['id'])!.value,
      ordre: this.editForm.get(['ordre'])!.value,
      famille: this.editForm.get(['famille'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAPGII>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
