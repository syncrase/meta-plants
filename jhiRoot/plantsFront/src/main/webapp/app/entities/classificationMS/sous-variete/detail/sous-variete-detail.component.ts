import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISousVariete } from '../sous-variete.model';

@Component({
  selector: 'perma-sous-variete-detail',
  templateUrl: './sous-variete-detail.component.html',
})
export class SousVarieteDetailComponent implements OnInit {
  sousVariete: ISousVariete | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousVariete }) => {
      this.sousVariete = sousVariete;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
