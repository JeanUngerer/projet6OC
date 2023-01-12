import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './component/login/login.component';
import { TopNavComponent } from './component/layout/top-nav/top-nav.component';
import {HttpClientModule} from "@angular/common/http";
import {ReactiveFormsModule} from "@angular/forms";
import { ErrorMessageComponent } from './shared/components/error-message/error-message.component';
import { HomeComponent } from './component/home/home.component';
import {MatCheckboxModule} from "@angular/material/checkbox";
import { UserInfoComponent } from './component/user-info/user-info.component';
import { TransferComponent } from './component/transfer/transfer.component';
import { ContactsComponent } from './component/contacts/contacts.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    TopNavComponent,
    ErrorMessageComponent,
    HomeComponent,
    UserInfoComponent,
    TransferComponent,
    ContactsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatCheckboxModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
