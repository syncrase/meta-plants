import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICronquist } from '../cronquist.model';

@Component({
  selector: 'gp-cronquist-detail',
  templateUrl: './cronquist-detail.component.html',
})
export class CronquistDetailComponent implements OnInit {
  cronquist: ICronquist | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cronquist }) => {
      this.cronquist = cronquist;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
