import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISousEspece } from '../sous-espece.model';

@Component({
  selector: 'perma-sous-espece-detail',
  templateUrl: './sous-espece-detail.component.html',
})
export class SousEspeceDetailComponent implements OnInit {
  sousEspece: ISousEspece | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousEspece }) => {
      this.sousEspece = sousEspece;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
