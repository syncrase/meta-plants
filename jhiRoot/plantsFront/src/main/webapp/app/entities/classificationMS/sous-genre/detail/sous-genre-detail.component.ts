import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISousGenre } from '../sous-genre.model';

@Component({
  selector: 'perma-sous-genre-detail',
  templateUrl: './sous-genre-detail.component.html',
})
export class SousGenreDetailComponent implements OnInit {
  sousGenre: ISousGenre | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousGenre }) => {
      this.sousGenre = sousGenre;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
