import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMicroOrdre, MicroOrdre } from '../micro-ordre.model';
import { MicroOrdreService } from '../service/micro-ordre.service';
import { IInfraOrdre } from 'app/entities/classificationMS/infra-ordre/infra-ordre.model';
import { InfraOrdreService } from 'app/entities/classificationMS/infra-ordre/service/infra-ordre.service';

@Component({
  selector: 'perma-micro-ordre-update',
  templateUrl: './micro-ordre-update.component.html',
})
export class MicroOrdreUpdateComponent implements OnInit {
  isSaving = false;

  microOrdresSharedCollection: IMicroOrdre[] = [];
  infraOrdresSharedCollection: IInfraOrdre[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    infraOrdre: [],
    microOrdre: [],
  });

  constructor(
    protected microOrdreService: MicroOrdreService,
    protected infraOrdreService: InfraOrdreService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ microOrdre }) => {
      this.updateForm(microOrdre);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const microOrdre = this.createFromForm();
    if (microOrdre.id !== undefined) {
      this.subscribeToSaveResponse(this.microOrdreService.update(microOrdre));
    } else {
      this.subscribeToSaveResponse(this.microOrdreService.create(microOrdre));
    }
  }

  trackMicroOrdreById(index: number, item: IMicroOrdre): number {
    return item.id!;
  }

  trackInfraOrdreById(index: number, item: IInfraOrdre): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMicroOrdre>>): void {
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

  protected updateForm(microOrdre: IMicroOrdre): void {
    this.editForm.patchValue({
      id: microOrdre.id,
      nomFr: microOrdre.nomFr,
      nomLatin: microOrdre.nomLatin,
      infraOrdre: microOrdre.infraOrdre,
      microOrdre: microOrdre.microOrdre,
    });

    this.microOrdresSharedCollection = this.microOrdreService.addMicroOrdreToCollectionIfMissing(
      this.microOrdresSharedCollection,
      microOrdre.microOrdre
    );
    this.infraOrdresSharedCollection = this.infraOrdreService.addInfraOrdreToCollectionIfMissing(
      this.infraOrdresSharedCollection,
      microOrdre.infraOrdre
    );
  }

  protected loadRelationshipsOptions(): void {
    this.microOrdreService
      .query()
      .pipe(map((res: HttpResponse<IMicroOrdre[]>) => res.body ?? []))
      .pipe(
        map((microOrdres: IMicroOrdre[]) =>
          this.microOrdreService.addMicroOrdreToCollectionIfMissing(microOrdres, this.editForm.get('microOrdre')!.value)
        )
      )
      .subscribe((microOrdres: IMicroOrdre[]) => (this.microOrdresSharedCollection = microOrdres));

    this.infraOrdreService
      .query()
      .pipe(map((res: HttpResponse<IInfraOrdre[]>) => res.body ?? []))
      .pipe(
        map((infraOrdres: IInfraOrdre[]) =>
          this.infraOrdreService.addInfraOrdreToCollectionIfMissing(infraOrdres, this.editForm.get('infraOrdre')!.value)
        )
      )
      .subscribe((infraOrdres: IInfraOrdre[]) => (this.infraOrdresSharedCollection = infraOrdres));
  }

  protected createFromForm(): IMicroOrdre {
    return {
      ...new MicroOrdre(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      infraOrdre: this.editForm.get(['infraOrdre'])!.value,
      microOrdre: this.editForm.get(['microOrdre'])!.value,
    };
  }
}
