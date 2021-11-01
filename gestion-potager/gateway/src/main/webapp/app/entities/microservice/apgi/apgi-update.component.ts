import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAPGI, APGI } from 'app/shared/model/microservice/apgi.model';
import { APGIService } from './apgi.service';

@Component({
  selector: 'gp-apgi-update',
  templateUrl: './apgi-update.component.html',
})
export class APGIUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    ordre: [null, [Validators.required]],
    famille: [null, [Validators.required]],
  });

  constructor(protected aPGIService: APGIService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGI }) => {
      this.updateForm(aPGI);
    });
  }

  updateForm(aPGI: IAPGI): void {
    this.editForm.patchValue({
      id: aPGI.id,
      ordre: aPGI.ordre,
      famille: aPGI.famille,
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

  private createFromForm(): IAPGI {
    return {
      ...new APGI(),
      id: this.editForm.get(['id'])!.value,
      ordre: this.editForm.get(['ordre'])!.value,
      famille: this.editForm.get(['famille'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAPGI>>): void {
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
