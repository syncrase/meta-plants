import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVariete } from '../variete.model';

@Component({
  selector: 'perma-variete-detail',
  templateUrl: './variete-detail.component.html',
})
export class VarieteDetailComponent implements OnInit {
  variete: IVariete | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ variete }) => {
      this.variete = variete;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
