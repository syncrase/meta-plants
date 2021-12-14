import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISousClasse } from '../sous-classe.model';

@Component({
  selector: 'perma-sous-classe-detail',
  templateUrl: './sous-classe-detail.component.html',
})
export class SousClasseDetailComponent implements OnInit {
  sousClasse: ISousClasse | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousClasse }) => {
      this.sousClasse = sousClasse;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
