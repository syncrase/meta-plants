import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRaunkierPlante } from '../raunkier-plante.model';

@Component({
  selector: 'perma-raunkier-plante-detail',
  templateUrl: './raunkier-plante-detail.component.html',
})
export class RaunkierPlanteDetailComponent implements OnInit {
  raunkierPlante: IRaunkierPlante | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ raunkierPlante }) => {
      this.raunkierPlante = raunkierPlante;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
