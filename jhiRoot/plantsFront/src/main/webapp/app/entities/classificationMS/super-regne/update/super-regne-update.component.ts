import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISuperRegne, SuperRegne } from '../super-regne.model';
import { SuperRegneService } from '../service/super-regne.service';

@Component({
  selector: 'perma-super-regne-update',
  templateUrl: './super-regne-update.component.html',
})
export class SuperRegneUpdateComponent implements OnInit {
  isSaving = false;

  superRegnesSharedCollection: ISuperRegne[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    superRegne: [],
  });

  constructor(protected superRegneService: SuperRegneService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ superRegne }) => {
      this.updateForm(superRegne);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const superRegne = this.createFromForm();
    if (superRegne.id !== undefined) {
      this.subscribeToSaveResponse(this.superRegneService.update(superRegne));
    } else {
      this.subscribeToSaveResponse(this.superRegneService.create(superRegne));
    }
  }

  trackSuperRegneById(index: number, item: ISuperRegne): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISuperRegne>>): void {
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

  protected updateForm(superRegne: ISuperRegne): void {
    this.editForm.patchValue({
      id: superRegne.id,
      nomFr: superRegne.nomFr,
      nomLatin: superRegne.nomLatin,
      superRegne: superRegne.superRegne,
    });

    this.superRegnesSharedCollection = this.superRegneService.addSuperRegneToCollectionIfMissing(
      this.superRegnesSharedCollection,
      superRegne.superRegne
    );
  }

  protected loadRelationshipsOptions(): void {
    this.superRegneService
      .query()
      .pipe(map((res: HttpResponse<ISuperRegne[]>) => res.body ?? []))
      .pipe(
        map((superRegnes: ISuperRegne[]) =>
          this.superRegneService.addSuperRegneToCollectionIfMissing(superRegnes, this.editForm.get('superRegne')!.value)
        )
      )
      .subscribe((superRegnes: ISuperRegne[]) => (this.superRegnesSharedCollection = superRegnes));
  }

  protected createFromForm(): ISuperRegne {
    return {
      ...new SuperRegne(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      superRegne: this.editForm.get(['superRegne'])!.value,
    };
  }
}
