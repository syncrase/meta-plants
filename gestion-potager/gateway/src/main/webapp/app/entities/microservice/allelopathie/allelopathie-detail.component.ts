import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAllelopathie } from 'app/shared/model/microservice/allelopathie.model';

@Component({
  selector: 'gp-allelopathie-detail',
  templateUrl: './allelopathie-detail.component.html',
})
export class AllelopathieDetailComponent implements OnInit {
  allelopathie: IAllelopathie | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ allelopathie }) => (this.allelopathie = allelopathie));
  }

  previousState(): void {
    window.history.back();
  }
}
