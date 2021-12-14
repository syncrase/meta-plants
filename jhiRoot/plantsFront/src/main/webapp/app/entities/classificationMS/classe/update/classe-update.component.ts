import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClasse, Classe } from '../classe.model';
import { ClasseService } from '../service/classe.service';
import { ISuperClasse } from 'app/entities/classificationMS/super-classe/super-classe.model';
import { SuperClasseService } from 'app/entities/classificationMS/super-classe/service/super-classe.service';

@Component({
  selector: 'perma-classe-update',
  templateUrl: './classe-update.component.html',
})
export class ClasseUpdateComponent implements OnInit {
  isSaving = false;

  classesSharedCollection: IClasse[] = [];
  superClassesSharedCollection: ISuperClasse[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    superClasse: [],
    classe: [],
  });

  constructor(
    protected classeService: ClasseService,
    protected superClasseService: SuperClasseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classe }) => {
      this.updateForm(classe);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classe = this.createFromForm();
    if (classe.id !== undefined) {
      this.subscribeToSaveResponse(this.classeService.update(classe));
    } else {
      this.subscribeToSaveResponse(this.classeService.create(classe));
    }
  }

  trackClasseById(index: number, item: IClasse): number {
    return item.id!;
  }

  trackSuperClasseById(index: number, item: ISuperClasse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClasse>>): void {
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

  protected updateForm(classe: IClasse): void {
    this.editForm.patchValue({
      id: classe.id,
      nomFr: classe.nomFr,
      nomLatin: classe.nomLatin,
      superClasse: classe.superClasse,
      classe: classe.classe,
    });

    this.classesSharedCollection = this.classeService.addClasseToCollectionIfMissing(this.classesSharedCollection, classe.classe);
    this.superClassesSharedCollection = this.superClasseService.addSuperClasseToCollectionIfMissing(
      this.superClassesSharedCollection,
      classe.superClasse
    );
  }

  protected loadRelationshipsOptions(): void {
    this.classeService
      .query()
      .pipe(map((res: HttpResponse<IClasse[]>) => res.body ?? []))
      .pipe(map((classes: IClasse[]) => this.classeService.addClasseToCollectionIfMissing(classes, this.editForm.get('classe')!.value)))
      .subscribe((classes: IClasse[]) => (this.classesSharedCollection = classes));

    this.superClasseService
      .query()
      .pipe(map((res: HttpResponse<ISuperClasse[]>) => res.body ?? []))
      .pipe(
        map((superClasses: ISuperClasse[]) =>
          this.superClasseService.addSuperClasseToCollectionIfMissing(superClasses, this.editForm.get('superClasse')!.value)
        )
      )
      .subscribe((superClasses: ISuperClasse[]) => (this.superClassesSharedCollection = superClasses));
  }

  protected createFromForm(): IClasse {
    return {
      ...new Classe(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      superClasse: this.editForm.get(['superClasse'])!.value,
      classe: this.editForm.get(['classe'])!.value,
    };
  }
}
