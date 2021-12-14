import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInfraRegne, InfraRegne } from '../infra-regne.model';
import { InfraRegneService } from '../service/infra-regne.service';
import { IRameau } from 'app/entities/classificationMS/rameau/rameau.model';
import { RameauService } from 'app/entities/classificationMS/rameau/service/rameau.service';

@Component({
  selector: 'perma-infra-regne-update',
  templateUrl: './infra-regne-update.component.html',
})
export class InfraRegneUpdateComponent implements OnInit {
  isSaving = false;

  infraRegnesSharedCollection: IInfraRegne[] = [];
  rameausSharedCollection: IRameau[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [null, [Validators.required]],
    nomLatin: [],
    rameau: [],
    infraRegne: [],
  });

  constructor(
    protected infraRegneService: InfraRegneService,
    protected rameauService: RameauService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infraRegne }) => {
      this.updateForm(infraRegne);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const infraRegne = this.createFromForm();
    if (infraRegne.id !== undefined) {
      this.subscribeToSaveResponse(this.infraRegneService.update(infraRegne));
    } else {
      this.subscribeToSaveResponse(this.infraRegneService.create(infraRegne));
    }
  }

  trackInfraRegneById(index: number, item: IInfraRegne): number {
    return item.id!;
  }

  trackRameauById(index: number, item: IRameau): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInfraRegne>>): void {
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

  protected updateForm(infraRegne: IInfraRegne): void {
    this.editForm.patchValue({
      id: infraRegne.id,
      nomFr: infraRegne.nomFr,
      nomLatin: infraRegne.nomLatin,
      rameau: infraRegne.rameau,
      infraRegne: infraRegne.infraRegne,
    });

    this.infraRegnesSharedCollection = this.infraRegneService.addInfraRegneToCollectionIfMissing(
      this.infraRegnesSharedCollection,
      infraRegne.infraRegne
    );
    this.rameausSharedCollection = this.rameauService.addRameauToCollectionIfMissing(this.rameausSharedCollection, infraRegne.rameau);
  }

  protected loadRelationshipsOptions(): void {
    this.infraRegneService
      .query()
      .pipe(map((res: HttpResponse<IInfraRegne[]>) => res.body ?? []))
      .pipe(
        map((infraRegnes: IInfraRegne[]) =>
          this.infraRegneService.addInfraRegneToCollectionIfMissing(infraRegnes, this.editForm.get('infraRegne')!.value)
        )
      )
      .subscribe((infraRegnes: IInfraRegne[]) => (this.infraRegnesSharedCollection = infraRegnes));

    this.rameauService
      .query()
      .pipe(map((res: HttpResponse<IRameau[]>) => res.body ?? []))
      .pipe(map((rameaus: IRameau[]) => this.rameauService.addRameauToCollectionIfMissing(rameaus, this.editForm.get('rameau')!.value)))
      .subscribe((rameaus: IRameau[]) => (this.rameausSharedCollection = rameaus));
  }

  protected createFromForm(): IInfraRegne {
    return {
      ...new InfraRegne(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomLatin: this.editForm.get(['nomLatin'])!.value,
      rameau: this.editForm.get(['rameau'])!.value,
      infraRegne: this.editForm.get(['infraRegne'])!.value,
    };
  }
}
