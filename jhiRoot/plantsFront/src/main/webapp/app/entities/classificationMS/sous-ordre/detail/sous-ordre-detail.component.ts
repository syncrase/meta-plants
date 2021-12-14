import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISousOrdre } from '../sous-ordre.model';

@Component({
  selector: 'perma-sous-ordre-detail',
  templateUrl: './sous-ordre-detail.component.html',
})
export class SousOrdreDetailComponent implements OnInit {
  sousOrdre: ISousOrdre | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousOrdre }) => {
      this.sousOrdre = sousOrdre;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
