import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISousForme } from '../sous-forme.model';

@Component({
  selector: 'perma-sous-forme-detail',
  templateUrl: './sous-forme-detail.component.html',
})
export class SousFormeDetailComponent implements OnInit {
  sousForme: ISousForme | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousForme }) => {
      this.sousForme = sousForme;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
