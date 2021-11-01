import { ErrorHandler, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ErrorAlerterService implements ErrorHandler {

  constructor() { }

  handleError(error: { stack: any; }) {
    alert(error);
    console.log(error);
  }

}
