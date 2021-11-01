import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMois, Mois } from 'app/shared/model/microservice/mois.model';
import { MoisService } from './mois.service';

@Component({
  selector: 'gp-mois-update',
  templateUrl: './mois-update.component.html',
})
export class MoisUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    numero: [null, [Validators.required]],
    nom: [null, [Validators.required]],
  });

  constructor(protected moisService: MoisService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mois }) => {
      this.updateForm(mois);
    });
  }

  updateForm(mois: IMois): void {
    this.editForm.patchValue({
      id: mois.id,
      numero: mois.numero,
      nom: mois.nom,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mois = this.createFromForm();
    if (mois.id !== undefined) {
      this.subscribeToSaveResponse(this.moisService.update(mois));
    } else {
      this.subscribeToSaveResponse(this.moisService.create(mois));
    }
  }

  private createFromForm(): IMois {
    return {
      ...new Mois(),
      id: this.editForm.get(['id'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      nom: this.editForm.get(['nom'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMois>>): void {
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
