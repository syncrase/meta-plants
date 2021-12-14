import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISousTribu } from '../sous-tribu.model';

@Component({
  selector: 'perma-sous-tribu-detail',
  templateUrl: './sous-tribu-detail.component.html',
})
export class SousTribuDetailComponent implements OnInit {
  sousTribu: ISousTribu | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousTribu }) => {
      this.sousTribu = sousTribu;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
