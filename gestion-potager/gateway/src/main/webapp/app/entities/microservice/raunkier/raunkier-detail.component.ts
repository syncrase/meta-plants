import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRaunkier } from 'app/shared/model/microservice/raunkier.model';

@Component({
  selector: 'gp-raunkier-detail',
  templateUrl: './raunkier-detail.component.html',
})
export class RaunkierDetailComponent implements OnInit {
  raunkier: IRaunkier | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ raunkier }) => (this.raunkier = raunkier));
  }

  previousState(): void {
    window.history.back();
  }
}
