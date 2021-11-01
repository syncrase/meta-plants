import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Mois } from '../../Mois';
import { MOIS } from '../../semis.component';

@Component({
  selector: 'ptg-periode',
  templateUrl: './periode.component.html',
  styleUrls: ['./periode.component.scss']
})
export class PeriodeComponent implements OnInit {

  public filtrePeriode: FormGroup;

  mois = MOIS;

  @Output()
  periodeChange = new EventEmitter();

  constructor(private formBuilder: FormBuilder) {
    this.filtrePeriode = this.formBuilder.group({ debut: ['Janvier'], fin: ['Décembre'] });
  }

  ngOnInit(): void {
    console.log('ngOnInit FiltresComponent');
    this.onChanges();
  }

  onChanges(): void {
    this.filtrePeriode.valueChanges.subscribe(val => {
      this.periodeChange.emit(val);
    });

  }
}
