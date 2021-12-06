import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAPGIV, APGIV } from '../apgiv.model';
import { APGIVService } from '../service/apgiv.service';

@Component({
  selector: 'jhi-apgiv-update',
  templateUrl: './apgiv-update.component.html',
})
export class APGIVUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    ordre: [null, [Validators.required]],
    famille: [null, [Validators.required]],
  });

  constructor(protected aPGIVService: APGIVService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGIV }) => {
      this.updateForm(aPGIV);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aPGIV = this.createFromForm();
    if (aPGIV.id !== undefined) {
      this.subscribeToSaveResponse(this.aPGIVService.update(aPGIV));
    } else {
      this.subscribeToSaveResponse(this.aPGIVService.create(aPGIV));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAPGIV>>): void {
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

  protected updateForm(aPGIV: IAPGIV): void {
    this.editForm.patchValue({
      id: aPGIV.id,
      ordre: aPGIV.ordre,
      famille: aPGIV.famille,
    });
  }

  protected createFromForm(): IAPGIV {
    return {
      ...new APGIV(),
      id: this.editForm.get(['id'])!.value,
      ordre: this.editForm.get(['ordre'])!.value,
      famille: this.editForm.get(['famille'])!.value,
    };
  }
}
