import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRegne } from '../regne.model';

@Component({
  selector: 'perma-regne-detail',
  templateUrl: './regne-detail.component.html',
})
export class RegneDetailComponent implements OnInit {
  regne: IRegne | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ regne }) => {
      this.regne = regne;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
