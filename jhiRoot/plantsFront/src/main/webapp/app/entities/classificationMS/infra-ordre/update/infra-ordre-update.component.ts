import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInfraOrdre, InfraOrdre } from '../infra-ordre.model';
import { InfraOrdreService } from '../service/infra-ordre.service';
import { ISousOrdre } from 'app/entities/classificationMS/sous-ordre/sous-ordre.model';
import { SousOrdreService } from 'app/entities/classificationMS/sous-ordre/service/sous-ordre.service';

@Component({
  selector: 'perma-infra-ordre-update',
  templateUrl: './infra-ordre-update.component.html',
})
export class InfraOrdreUpdateComponent implements OnInit {
  isSaving = false;

  infraOrdresSharedCollection: IInfraOrdre[] = [];
  sousOrdresSharedCollection: ISousOrdre[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    sousOrdre: [],
    infraOrdre: [],
  });

  constructor(
    protected infraOrdreService: InfraOrdreService,
    protected sousOrdreService: SousOrdreService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infraOrdre }) => {
      this.updateForm(infraOrdre);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const infraOrdre = this.createFromForm();
    if (infraOrdre.id !== undefined) {
      this.subscribeToSaveResponse(this.infraOrdreService.update(infraOrdre));
    } else {
      this.subscribeToSaveResponse(this.infraOrdreService.create(infraOrdre));
    }
  }

  trackInfraOrdreById(index: number, item: IInfraOrdre): number {
    return item.id!;
  }

  trackSousOrdreById(index: number, item: ISousOrdre): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInfraOrdre>>): void {
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

  protected updateForm(infraOrdre: IInfraOrdre): void {
    this.editForm.patchValue({
      id: infraOrdre.id,
      nomFr: infraOrdre.nomFr,
      nomLatin: infraOrdre.nomLatin,
      sousOrdre: infraOrdre.sousOrdre,
      infraOrdre: infraOrdre.infraOrdre,
    });

    this.infraOrdresSharedCollection = this.infraOrdreService.addInfraOrdreToCollectionIfMissing(
      this.infraOrdresSharedCollection,
      infraOrdre.infraOrdre
    );
    this.sousOrdresSharedCollection = this.sousOrdreService.addSousOrdreToCollectionIfMissing(
      this.sousOrdresSharedCollection,
      infraOrdre.sousOrdre
    );
  }

  protected loadRelationshipsOptions(): void {
    this.infraOrdreService
      .query()
      .pipe(map((res: HttpResponse<IInfraOrdre[]>) => res.body ?? []))
      .pipe(
        map((infraOrdres: IInfraOrdre[]) =>
          this.infraOrdreService.addInfraOrdreToCollectionIfMissing(infraOrdres, this.editForm.get('infraOrdre')!.value)
        )
      )
      .subscribe((infraOrdres: IInfraOrdre[]) => (this.infraOrdresSharedCollection = infraOrdres));

    this.sousOrdreService
      .query()
      .pipe(map((res: HttpResponse<ISousOrdre[]>) => res.body ?? []))
      .pipe(
        map((sousOrdres: ISousOrdre[]) =>
          this.sousOrdreService.addSousOrdreToCollectionIfMissing(sousOrdres, this.editForm.get('sousOrdre')!.value)
        )
      )
      .subscribe((sousOrdres: ISousOrdre[]) => (this.sousOrdresSharedCollection = sousOrdres));
  }

  protected createFromForm(): IInfraOrdre {
    return {
      ...new InfraOrdre(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      sousOrdre: this.editForm.get(['sousOrdre'])!.value,
      infraOrdre: this.editForm.get(['infraOrdre'])!.value,
    };
  }
}
