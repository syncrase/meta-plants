import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAPGIV, APGIV } from 'app/shared/model/microservice/apgiv.model';
import { APGIVService } from './apgiv.service';

@Component({
  selector: 'gp-apgiv-update',
  templateUrl: './apgiv-update.component.html',
})
export class APGIVUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    ordre: [null, [Validators.required]],
    famille: [null, [Validators.required]],
  });

  constructor(protected aPGIVService: APGIVService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGIV }) => {
      this.updateForm(aPGIV);
    });
  }

  updateForm(aPGIV: IAPGIV): void {
    this.editForm.patchValue({
      id: aPGIV.id,
      ordre: aPGIV.ordre,
      famille: aPGIV.famille,
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

  private createFromForm(): IAPGIV {
    return {
      ...new APGIV(),
      id: this.editForm.get(['id'])!.value,
      ordre: this.editForm.get(['ordre'])!.value,
      famille: this.editForm.get(['famille'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAPGIV>>): void {
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
