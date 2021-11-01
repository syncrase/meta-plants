import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITypeSemis, TypeSemis } from 'app/shared/model/microservice/type-semis.model';
import { TypeSemisService } from './type-semis.service';

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

  constructor(protected typeSemisService: TypeSemisService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeSemis }) => {
      this.updateForm(typeSemis);
    });
  }

  updateForm(typeSemis: ITypeSemis): void {
    this.editForm.patchValue({
      id: typeSemis.id,
      description: typeSemis.description,
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

  private createFromForm(): ITypeSemis {
    return {
      ...new TypeSemis(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeSemis>>): void {
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
