import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMicroEmbranchement, MicroEmbranchement } from '../micro-embranchement.model';
import { MicroEmbranchementService } from '../service/micro-embranchement.service';
import { IInfraEmbranchement } from 'app/entities/classificationMS/infra-embranchement/infra-embranchement.model';
import { InfraEmbranchementService } from 'app/entities/classificationMS/infra-embranchement/service/infra-embranchement.service';

@Component({
  selector: 'perma-micro-embranchement-update',
  templateUrl: './micro-embranchement-update.component.html',
})
export class MicroEmbranchementUpdateComponent implements OnInit {
  isSaving = false;

  microEmbranchementsSharedCollection: IMicroEmbranchement[] = [];
  infraEmbranchementsSharedCollection: IInfraEmbranchement[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    infraEmbranchement: [],
    microEmbranchement: [],
  });

  constructor(
    protected microEmbranchementService: MicroEmbranchementService,
    protected infraEmbranchementService: InfraEmbranchementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ microEmbranchement }) => {
      this.updateForm(microEmbranchement);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const microEmbranchement = this.createFromForm();
    if (microEmbranchement.id !== undefined) {
      this.subscribeToSaveResponse(this.microEmbranchementService.update(microEmbranchement));
    } else {
      this.subscribeToSaveResponse(this.microEmbranchementService.create(microEmbranchement));
    }
  }

  trackMicroEmbranchementById(index: number, item: IMicroEmbranchement): number {
    return item.id!;
  }

  trackInfraEmbranchementById(index: number, item: IInfraEmbranchement): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMicroEmbranchement>>): void {
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

  protected updateForm(microEmbranchement: IMicroEmbranchement): void {
    this.editForm.patchValue({
      id: microEmbranchement.id,
      nomFr: microEmbranchement.nomFr,
      nomLatin: microEmbranchement.nomLatin,
      infraEmbranchement: microEmbranchement.infraEmbranchement,
      microEmbranchement: microEmbranchement.microEmbranchement,
    });

    this.microEmbranchementsSharedCollection = this.microEmbranchementService.addMicroEmbranchementToCollectionIfMissing(
      this.microEmbranchementsSharedCollection,
      microEmbranchement.microEmbranchement
    );
    this.infraEmbranchementsSharedCollection = this.infraEmbranchementService.addInfraEmbranchementToCollectionIfMissing(
      this.infraEmbranchementsSharedCollection,
      microEmbranchement.infraEmbranchement
    );
  }

  protected loadRelationshipsOptions(): void {
    this.microEmbranchementService
      .query()
      .pipe(map((res: HttpResponse<IMicroEmbranchement[]>) => res.body ?? []))
      .pipe(
        map((microEmbranchements: IMicroEmbranchement[]) =>
          this.microEmbranchementService.addMicroEmbranchementToCollectionIfMissing(
            microEmbranchements,
            this.editForm.get('microEmbranchement')!.value
          )
        )
      )
      .subscribe((microEmbranchements: IMicroEmbranchement[]) => (this.microEmbranchementsSharedCollection = microEmbranchements));

    this.infraEmbranchementService
      .query()
      .pipe(map((res: HttpResponse<IInfraEmbranchement[]>) => res.body ?? []))
      .pipe(
        map((infraEmbranchements: IInfraEmbranchement[]) =>
          this.infraEmbranchementService.addInfraEmbranchementToCollectionIfMissing(
            infraEmbranchements,
            this.editForm.get('infraEmbranchement')!.value
          )
        )
      )
      .subscribe((infraEmbranchements: IInfraEmbranchement[]) => (this.infraEmbranchementsSharedCollection = infraEmbranchements));
  }

  protected createFromForm(): IMicroEmbranchement {
    return {
      ...new MicroEmbranchement(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      infraEmbranchement: this.editForm.get(['infraEmbranchement'])!.value,
      microEmbranchement: this.editForm.get(['microEmbranchement'])!.value,
    };
  }
}
