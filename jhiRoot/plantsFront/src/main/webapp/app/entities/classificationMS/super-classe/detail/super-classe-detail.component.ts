import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISuperClasse } from '../super-classe.model';

@Component({
  selector: 'perma-super-classe-detail',
  templateUrl: './super-classe-detail.component.html',
})
export class SuperClasseDetailComponent implements OnInit {
  superClasse: ISuperClasse | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ superClasse }) => {
      this.superClasse = superClasse;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
