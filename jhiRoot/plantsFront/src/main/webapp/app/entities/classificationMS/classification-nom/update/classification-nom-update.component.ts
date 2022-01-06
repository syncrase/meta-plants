import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClassificationNom, ClassificationNom } from '../classification-nom.model';
import { ClassificationNomService } from '../service/classification-nom.service';
import { ICronquistRank } from 'app/entities/classificationMS/cronquist-rank/cronquist-rank.model';
import { CronquistRankService } from 'app/entities/classificationMS/cronquist-rank/service/cronquist-rank.service';

@Component({
  selector: 'perma-classification-nom-update',
  templateUrl: './classification-nom-update.component.html',
})
export class ClassificationNomUpdateComponent implements OnInit {
  isSaving = false;

  cronquistRanksSharedCollection: ICronquistRank[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, []],
    nomLatin: [null, []],
    cronquistRank: [null, Validators.required],
  });

  constructor(
    protected classificationNomService: ClassificationNomService,
    protected cronquistRankService: CronquistRankService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classificationNom }) => {
      this.updateForm(classificationNom);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classificationNom = this.createFromForm();
    if (classificationNom.id !== undefined) {
      this.subscribeToSaveResponse(this.classificationNomService.update(classificationNom));
    } else {
      this.subscribeToSaveResponse(this.classificationNomService.create(classificationNom));
    }
  }

  trackCronquistRankById(index: number, item: ICronquistRank): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassificationNom>>): void {
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

  protected updateForm(classificationNom: IClassificationNom): void {
    this.editForm.patchValue({
      id: classificationNom.id,
      nomFr: classificationNom.nomFr,
      nomLatin: classificationNom.nomLatin,
      cronquistRank: classificationNom.cronquistRank,
    });

    this.cronquistRanksSharedCollection = this.cronquistRankService.addCronquistRankToCollectionIfMissing(
      this.cronquistRanksSharedCollection,
      classificationNom.cronquistRank
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cronquistRankService
      .query()
      .pipe(map((res: HttpResponse<ICronquistRank[]>) => res.body ?? []))
      .pipe(
        map((cronquistRanks: ICronquistRank[]) =>
          this.cronquistRankService.addCronquistRankToCollectionIfMissing(cronquistRanks, this.editForm.get('cronquistRank')!.value)
        )
      )
      .subscribe((cronquistRanks: ICronquistRank[]) => (this.cronquistRanksSharedCollection = cronquistRanks));
  }

  protected createFromForm(): IClassificationNom {
    return {
      ...new ClassificationNom(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      cronquistRank: this.editForm.get(['cronquistRank'])!.value,
    };
  }
}
