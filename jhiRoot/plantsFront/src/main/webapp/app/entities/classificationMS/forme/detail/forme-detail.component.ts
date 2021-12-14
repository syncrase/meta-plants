import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IForme } from '../forme.model';

@Component({
  selector: 'perma-forme-detail',
  templateUrl: './forme-detail.component.html',
})
export class FormeDetailComponent implements OnInit {
  forme: IForme | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ forme }) => {
      this.forme = forme;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
