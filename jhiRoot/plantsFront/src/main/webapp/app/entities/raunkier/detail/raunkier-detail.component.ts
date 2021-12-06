import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRaunkier } from '../raunkier.model';

@Component({
  selector: 'jhi-raunkier-detail',
  templateUrl: './raunkier-detail.component.html',
})
export class RaunkierDetailComponent implements OnInit {
  raunkier: IRaunkier | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ raunkier }) => {
      this.raunkier = raunkier;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
