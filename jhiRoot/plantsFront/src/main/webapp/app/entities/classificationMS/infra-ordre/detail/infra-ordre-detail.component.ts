import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInfraOrdre } from '../infra-ordre.model';

@Component({
  selector: 'perma-infra-ordre-detail',
  templateUrl: './infra-ordre-detail.component.html',
})
export class InfraOrdreDetailComponent implements OnInit {
  infraOrdre: IInfraOrdre | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infraOrdre }) => {
      this.infraOrdre = infraOrdre;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
