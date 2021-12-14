import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAPGIVPlante } from '../apgiv-plante.model';

@Component({
  selector: 'perma-apgiv-plante-detail',
  templateUrl: './apgiv-plante-detail.component.html',
})
export class APGIVPlanteDetailComponent implements OnInit {
  aPGIVPlante: IAPGIVPlante | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aPGIVPlante }) => {
      this.aPGIVPlante = aPGIVPlante;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
