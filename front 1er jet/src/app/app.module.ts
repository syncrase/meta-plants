import { BrowserModule } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './layout/home/home.component';
import { PagesModule } from './pages/pages.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ErrorAlerterService } from './ngoverride/error-alerter.service';
import { ToastsContainer } from './shared/toast/toasts-container.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    ToastsContainer
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    PagesModule
  ],
  providers: [
    {
      provide: ErrorHandler,
      useClass: ErrorAlerterService,
    }],
  bootstrap: [AppComponent]
})
export class AppModule { }
