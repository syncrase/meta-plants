import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISousDivision, SousDivision } from '../sous-division.model';
import { SousDivisionService } from '../service/sous-division.service';
import { IDivision } from 'app/entities/classificationMS/division/division.model';
import { DivisionService } from 'app/entities/classificationMS/division/service/division.service';

@Component({
  selector: 'perma-sous-division-update',
  templateUrl: './sous-division-update.component.html',
})
export class SousDivisionUpdateComponent implements OnInit {
  isSaving = false;

  sousDivisionsSharedCollection: ISousDivision[] = [];
  divisionsSharedCollection: IDivision[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    division: [],
    sousDivision: [],
  });

  constructor(
    protected sousDivisionService: SousDivisionService,
    protected divisionService: DivisionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousDivision }) => {
      this.updateForm(sousDivision);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sousDivision = this.createFromForm();
    if (sousDivision.id !== undefined) {
      this.subscribeToSaveResponse(this.sousDivisionService.update(sousDivision));
    } else {
      this.subscribeToSaveResponse(this.sousDivisionService.create(sousDivision));
    }
  }

  trackSousDivisionById(index: number, item: ISousDivision): number {
    return item.id!;
  }

  trackDivisionById(index: number, item: IDivision): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISousDivision>>): void {
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

  protected updateForm(sousDivision: ISousDivision): void {
    this.editForm.patchValue({
      id: sousDivision.id,
      nomFr: sousDivision.nomFr,
      nomLatin: sousDivision.nomLatin,
      division: sousDivision.division,
      sousDivision: sousDivision.sousDivision,
    });

    this.sousDivisionsSharedCollection = this.sousDivisionService.addSousDivisionToCollectionIfMissing(
      this.sousDivisionsSharedCollection,
      sousDivision.sousDivision
    );
    this.divisionsSharedCollection = this.divisionService.addDivisionToCollectionIfMissing(
      this.divisionsSharedCollection,
      sousDivision.division
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sousDivisionService
      .query()
      .pipe(map((res: HttpResponse<ISousDivision[]>) => res.body ?? []))
      .pipe(
        map((sousDivisions: ISousDivision[]) =>
          this.sousDivisionService.addSousDivisionToCollectionIfMissing(sousDivisions, this.editForm.get('sousDivision')!.value)
        )
      )
      .subscribe((sousDivisions: ISousDivision[]) => (this.sousDivisionsSharedCollection = sousDivisions));

    this.divisionService
      .query()
      .pipe(map((res: HttpResponse<IDivision[]>) => res.body ?? []))
      .pipe(
        map((divisions: IDivision[]) =>
          this.divisionService.addDivisionToCollectionIfMissing(divisions, this.editForm.get('division')!.value)
        )
      )
      .subscribe((divisions: IDivision[]) => (this.divisionsSharedCollection = divisions));
  }

  protected createFromForm(): ISousDivision {
    return {
      ...new SousDivision(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      division: this.editForm.get(['division'])!.value,
      sousDivision: this.editForm.get(['sousDivision'])!.value,
    };
  }
}
