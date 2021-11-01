import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'ptg-example',
  templateUrl: './example.component.html',
  styleUrls: ['./example.component.scss']
})
export class ExampleComponent implements OnInit {


  title = 'gestion-potager2';

  constructor() { }

  ngOnInit(): void {
  }

}
