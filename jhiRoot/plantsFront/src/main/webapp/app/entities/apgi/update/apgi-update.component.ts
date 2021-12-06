import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAPGI, APGI } from '../apgi.model';
import { APGIService } from '../service/apgi.service';

@Component({
  selector: 'jhi-apgi-update',
  templateUrl: './apgi-update.component.html',
})
export class APGIUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    ordre: [null, [Validators.required]],
    famille: [null, [Validators.required]],
  });

  constructor(protected aPGIService: APGIService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGI }) => {
      this.updateForm(aPGI);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const aPGI = this.createFromForm();
    if (aPGI.id !== undefined) {
      this.subscribeToSaveResponse(this.aPGIService.update(aPGI));
    } else {
      this.subscribeToSaveResponse(this.aPGIService.create(aPGI));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAPGI>>): void {
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

  protected updateForm(aPGI: IAPGI): void {
    this.editForm.patchValue({
      id: aPGI.id,
      ordre: aPGI.ordre,
      famille: aPGI.famille,
    });
  }

  protected createFromForm(): IAPGI {
    return {
      ...new APGI(),
      id: this.editForm.get(['id'])!.value,
      ordre: this.editForm.get(['ordre'])!.value,
      famille: this.editForm.get(['famille'])!.value,
    };
  }
}
