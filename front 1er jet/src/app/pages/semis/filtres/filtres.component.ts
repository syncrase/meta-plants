import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';



@Component({
  selector: 'ptg-filtres',
  templateUrl: './filtres.component.html',
  styleUrls: ['./filtres.component.scss']
})
export class FiltresComponent implements OnInit {

  @Output()
  methodeSemisChange = new EventEmitter();

  @Output()
  periodeChange = new EventEmitter();

  constructor() {
    console.log('Constructor FiltresComponent');
  }

  ngOnInit() {
    console.log('ngOnInit FiltresComponent');

  }

  onMethodeSemisChange(event: any) {
    this.methodeSemisChange.emit(event);
  }

  onPeriodeChange(event: any) {
    this.periodeChange.emit(event);
  }
}
