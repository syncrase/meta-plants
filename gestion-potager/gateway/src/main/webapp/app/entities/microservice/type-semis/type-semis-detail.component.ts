import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypeSemis } from 'app/shared/model/microservice/type-semis.model';

@Component({
  selector: 'gp-type-semis-detail',
  templateUrl: './type-semis-detail.component.html',
})
export class TypeSemisDetailComponent implements OnInit {
  typeSemis: ITypeSemis | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeSemis }) => (this.typeSemis = typeSemis));
  }

  previousState(): void {
    window.history.back();
  }
}