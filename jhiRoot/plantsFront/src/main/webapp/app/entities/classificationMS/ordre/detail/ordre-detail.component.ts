import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrdre } from '../ordre.model';

@Component({
  selector: 'perma-ordre-detail',
  templateUrl: './ordre-detail.component.html',
})
export class OrdreDetailComponent implements OnInit {
  ordre: IOrdre | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ordre }) => {
      this.ordre = ordre;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
