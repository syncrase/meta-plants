import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRegne, Regne } from '../regne.model';
import { RegneService } from '../service/regne.service';
import { ISuperRegne } from 'app/entities/classificationMS/super-regne/super-regne.model';
import { SuperRegneService } from 'app/entities/classificationMS/super-regne/service/super-regne.service';

@Component({
  selector: 'perma-regne-update',
  templateUrl: './regne-update.component.html',
})
export class RegneUpdateComponent implements OnInit {
  isSaving = false;

  regnesSharedCollection: IRegne[] = [];
  superRegnesSharedCollection: ISuperRegne[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    superRegne: [],
    regne: [],
  });

  constructor(
    protected regneService: RegneService,
    protected superRegneService: SuperRegneService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ regne }) => {
      this.updateForm(regne);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const regne = this.createFromForm();
    if (regne.id !== undefined) {
      this.subscribeToSaveResponse(this.regneService.update(regne));
    } else {
      this.subscribeToSaveResponse(this.regneService.create(regne));
    }
  }

  trackRegneById(index: number, item: IRegne): number {
    return item.id!;
  }

  trackSuperRegneById(index: number, item: ISuperRegne): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegne>>): void {
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

  protected updateForm(regne: IRegne): void {
    this.editForm.patchValue({
      id: regne.id,
      nomFr: regne.nomFr,
      nomLatin: regne.nomLatin,
      superRegne: regne.superRegne,
      regne: regne.regne,
    });

    this.regnesSharedCollection = this.regneService.addRegneToCollectionIfMissing(this.regnesSharedCollection, regne.regne);
    this.superRegnesSharedCollection = this.superRegneService.addSuperRegneToCollectionIfMissing(
      this.superRegnesSharedCollection,
      regne.superRegne
    );
  }

  protected loadRelationshipsOptions(): void {
    this.regneService
      .query()
      .pipe(map((res: HttpResponse<IRegne[]>) => res.body ?? []))
      .pipe(map((regnes: IRegne[]) => this.regneService.addRegneToCollectionIfMissing(regnes, this.editForm.get('regne')!.value)))
      .subscribe((regnes: IRegne[]) => (this.regnesSharedCollection = regnes));

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

  protected createFromForm(): IRegne {
    return {
      ...new Regne(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      superRegne: this.editForm.get(['superRegne'])!.value,
      regne: this.editForm.get(['regne'])!.value,
    };
  }
}
