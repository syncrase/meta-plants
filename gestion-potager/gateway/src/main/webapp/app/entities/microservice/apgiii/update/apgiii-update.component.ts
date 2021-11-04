import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAPGIII, APGIII } from '../apgiii.model';
import { APGIIIService } from '../service/apgiii.service';

@Component({
  selector: 'gp-apgiii-update',
  templateUrl: './apgiii-update.component.html',
})
export class APGIIIUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    ordre: [null, [Validators.required]],
    famille: [null, [Validators.required]],
  });

  constructor(protected aPGIIIService: APGIIIService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGIII }) => {
      this.updateForm(aPGIII);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aPGIII = this.createFromForm();
    if (aPGIII.id !== undefined) {
      this.subscribeToSaveResponse(this.aPGIIIService.update(aPGIII));
    } else {
      this.subscribeToSaveResponse(this.aPGIIIService.create(aPGIII));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAPGIII>>): void {
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

  protected updateForm(aPGIII: IAPGIII): void {
    this.editForm.patchValue({
      id: aPGIII.id,
      ordre: aPGIII.ordre,
      famille: aPGIII.famille,
    });
  }

  protected createFromForm(): IAPGIII {
    return {
      ...new APGIII(),
      id: this.editForm.get(['id'])!.value,
      ordre: this.editForm.get(['ordre'])!.value,
      famille: this.editForm.get(['famille'])!.value,
    };
  }
}
