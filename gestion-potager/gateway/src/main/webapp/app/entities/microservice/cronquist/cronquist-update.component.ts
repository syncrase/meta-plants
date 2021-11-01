import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ICronquist, Cronquist } from 'app/shared/model/microservice/cronquist.model';
import { CronquistService } from './cronquist.service';

@Component({
  selector: 'gp-cronquist-update',
  templateUrl: './cronquist-update.component.html',
})
export class CronquistUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    regne: [null, [Validators.required]],
    sousRegne: [null, [Validators.required]],
    division: [null, [Validators.required]],
    classe: [null, [Validators.required]],
    sousClasse: [null, [Validators.required]],
    ordre: [null, [Validators.required]],
    famille: [null, [Validators.required]],
    genre: [null, [Validators.required]],
  });

  constructor(protected cronquistService: CronquistService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cronquist }) => {
      this.updateForm(cronquist);
    });
  }

  updateForm(cronquist: ICronquist): void {
    this.editForm.patchValue({
      id: cronquist.id,
      regne: cronquist.regne,
      sousRegne: cronquist.sousRegne,
      division: cronquist.division,
      classe: cronquist.classe,
      sousClasse: cronquist.sousClasse,
      ordre: cronquist.ordre,
      famille: cronquist.famille,
      genre: cronquist.genre,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cronquist = this.createFromForm();
    if (cronquist.id !== undefined) {
      this.subscribeToSaveResponse(this.cronquistService.update(cronquist));
    } else {
      this.subscribeToSaveResponse(this.cronquistService.create(cronquist));
    }
  }

  private createFromForm(): ICronquist {
    return {
      ...new Cronquist(),
      id: this.editForm.get(['id'])!.value,
      regne: this.editForm.get(['regne'])!.value,
      sousRegne: this.editForm.get(['sousRegne'])!.value,
      division: this.editForm.get(['division'])!.value,
      classe: this.editForm.get(['classe'])!.value,
      sousClasse: this.editForm.get(['sousClasse'])!.value,
      ordre: this.editForm.get(['ordre'])!.value,
      famille: this.editForm.get(['famille'])!.value,
      genre: this.editForm.get(['genre'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICronquist>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
