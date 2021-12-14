import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISousRegne } from '../sous-regne.model';

@Component({
  selector: 'perma-sous-regne-detail',
  templateUrl: './sous-regne-detail.component.html',
})
export class SousRegneDetailComponent implements OnInit {
  sousRegne: ISousRegne | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousRegne }) => {
      this.sousRegne = sousRegne;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
