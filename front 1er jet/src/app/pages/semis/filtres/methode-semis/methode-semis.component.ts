import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';

export const filtreMethodeSemisDefault = {
  pleineTerre: true,
  sousAbris: true
};

@Component({
  selector: 'ptg-methode-semis',
  templateUrl: './methode-semis.component.html',
  styleUrls: ['./methode-semis.component.scss']
})
export class MethodeSemisComponent implements OnInit {

  public filtreMethodeSemis: FormGroup;

  @Output()
  methodeSemisChange = new EventEmitter();

  constructor(private formBuilder: FormBuilder) {
    console.log('Constructor FiltresComponent');
    this.filtreMethodeSemis = this.formBuilder.group(filtreMethodeSemisDefault);
  }

  ngOnInit(): void {
    console.log('ngOnInit FiltresComponent');
    this.onChanges();
    // this.pleineTerreChange();
  }

  /**
   * Ecoute tous les éléments
   */
  onChanges(): void {
    this.filtreMethodeSemis.valueChanges.subscribe(val => {
      this.methodeSemisChange.emit(val);
    });
  }

  /**
   * N'écoute qu'un seul élément
   */
  // pleineTerreChange(): void {
  //   this.filtreMethodeSemis.get('pleineTerre')?.valueChanges.subscribe(val => {
  //     this.methodeSemisChange.emit();
  //   });
  // }

}
