import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITypeSemis, TypeSemis } from '../type-semis.model';
import { TypeSemisService } from '../service/type-semis.service';

@Component({
  selector: 'gp-type-semis-update',
  templateUrl: './type-semis-update.component.html',
})
export class TypeSemisUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    description: [],
  });

  constructor(protected typeSemisService: TypeSemisService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeSemis }) => {
      this.updateForm(typeSemis);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeSemis = this.createFromForm();
    if (typeSemis.id !== undefined) {
      this.subscribeToSaveResponse(this.typeSemisService.update(typeSemis));
    } else {
      this.subscribeToSaveResponse(this.typeSemisService.create(typeSemis));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeSemis>>): void {
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

  protected updateForm(typeSemis: ITypeSemis): void {
    this.editForm.patchValue({
      id: typeSemis.id,
      description: typeSemis.description,
    });
  }

  protected createFromForm(): ITypeSemis {
    return {
      ...new TypeSemis(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
