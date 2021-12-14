import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISousSection } from '../sous-section.model';

@Component({
  selector: 'perma-sous-section-detail',
  templateUrl: './sous-section-detail.component.html',
})
export class SousSectionDetailComponent implements OnInit {
  sousSection: ISousSection | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousSection }) => {
      this.sousSection = sousSection;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
