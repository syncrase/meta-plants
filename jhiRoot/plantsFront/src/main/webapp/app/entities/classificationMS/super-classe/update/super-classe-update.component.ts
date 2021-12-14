import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISuperClasse, SuperClasse } from '../super-classe.model';
import { SuperClasseService } from '../service/super-classe.service';
import { IMicroEmbranchement } from 'app/entities/classificationMS/micro-embranchement/micro-embranchement.model';
import { MicroEmbranchementService } from 'app/entities/classificationMS/micro-embranchement/service/micro-embranchement.service';

@Component({
  selector: 'perma-super-classe-update',
  templateUrl: './super-classe-update.component.html',
})
export class SuperClasseUpdateComponent implements OnInit {
  isSaving = false;

  superClassesSharedCollection: ISuperClasse[] = [];
  microEmbranchementsSharedCollection: IMicroEmbranchement[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    microEmbranchement: [],
    superClasse: [],
  });

  constructor(
    protected superClasseService: SuperClasseService,
    protected microEmbranchementService: MicroEmbranchementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ superClasse }) => {
      this.updateForm(superClasse);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const superClasse = this.createFromForm();
    if (superClasse.id !== undefined) {
      this.subscribeToSaveResponse(this.superClasseService.update(superClasse));
    } else {
      this.subscribeToSaveResponse(this.superClasseService.create(superClasse));
    }
  }

  trackSuperClasseById(index: number, item: ISuperClasse): number {
    return item.id!;
  }

  trackMicroEmbranchementById(index: number, item: IMicroEmbranchement): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISuperClasse>>): void {
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

  protected updateForm(superClasse: ISuperClasse): void {
    this.editForm.patchValue({
      id: superClasse.id,
      nomFr: superClasse.nomFr,
      nomLatin: superClasse.nomLatin,
      microEmbranchement: superClasse.microEmbranchement,
      superClasse: superClasse.superClasse,
    });

    this.superClassesSharedCollection = this.superClasseService.addSuperClasseToCollectionIfMissing(
      this.superClassesSharedCollection,
      superClasse.superClasse
    );
    this.microEmbranchementsSharedCollection = this.microEmbranchementService.addMicroEmbranchementToCollectionIfMissing(
      this.microEmbranchementsSharedCollection,
      superClasse.microEmbranchement
    );
  }

  protected loadRelationshipsOptions(): void {
    this.superClasseService
      .query()
      .pipe(map((res: HttpResponse<ISuperClasse[]>) => res.body ?? []))
      .pipe(
        map((superClasses: ISuperClasse[]) =>
          this.superClasseService.addSuperClasseToCollectionIfMissing(superClasses, this.editForm.get('superClasse')!.value)
        )
      )
      .subscribe((superClasses: ISuperClasse[]) => (this.superClassesSharedCollection = superClasses));

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
  }

  protected createFromForm(): ISuperClasse {
    return {
      ...new SuperClasse(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      microEmbranchement: this.editForm.get(['microEmbranchement'])!.value,
      superClasse: this.editForm.get(['superClasse'])!.value,
    };
  }
}
