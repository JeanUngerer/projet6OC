import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./component/login/login.component";
import {HomeComponent} from "./component/home/home.component";
import {AuthGuard} from "./core/guards/auth.guard";
import {AppComponent} from "./app.component";
import {UserInfoComponent} from "./component/user-info/user-info.component";
import {TransferComponent} from "./component/transfer/transfer.component";
import {ContactsComponent} from "./component/contacts/contacts.component";

const routes: Routes = [
  {
    path: 'user',
    component: UserInfoComponent,
    canActivate: [AuthGuard],
    runGuardsAndResolvers: 'always',
  },
  {
    path: 'contact',
    component: ContactsComponent,
    canActivate: [AuthGuard],
    runGuardsAndResolvers: 'always',
  },
  {
    path: 'transfer',
    component: TransferComponent,
    canActivate: [AuthGuard],
    runGuardsAndResolvers: 'always',
  },
  {
    path: 'home',
    component: HomeComponent
  },
  {
    path: 'login',
    component: LoginComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
