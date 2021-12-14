import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMicroEmbranchement } from '../micro-embranchement.model';

@Component({
  selector: 'perma-micro-embranchement-detail',
  templateUrl: './micro-embranchement-detail.component.html',
})
export class MicroEmbranchementDetailComponent implements OnInit {
  microEmbranchement: IMicroEmbranchement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ microEmbranchement }) => {
      this.microEmbranchement = microEmbranchement;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
