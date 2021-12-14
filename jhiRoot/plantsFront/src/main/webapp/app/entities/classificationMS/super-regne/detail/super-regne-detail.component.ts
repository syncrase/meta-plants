import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISuperRegne } from '../super-regne.model';

@Component({
  selector: 'perma-super-regne-detail',
  templateUrl: './super-regne-detail.component.html',
})
export class SuperRegneDetailComponent implements OnInit {
  superRegne: ISuperRegne | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ superRegne }) => {
      this.superRegne = superRegne;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
