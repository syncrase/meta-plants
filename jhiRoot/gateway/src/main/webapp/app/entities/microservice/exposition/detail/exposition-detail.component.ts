import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExposition } from '../exposition.model';

@Component({
  selector: 'gp-exposition-detail',
  templateUrl: './exposition-detail.component.html',
})
export class ExpositionDetailComponent implements OnInit {
  exposition: IExposition | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exposition }) => {
      this.exposition = exposition;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
