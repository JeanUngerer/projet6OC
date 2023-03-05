import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./component/login/login.component";
import {GithubCallbackComponent} from "./component/login/providerCallbacks/github-callback/github-callback.component";
import {GoogleCallbackComponent} from "./component/login/providerCallbacks/google-callback/google-callback.component";
import {HomeComponent} from "./component/home/home.component";
import {AuthGuard} from "./core/guards/auth.guard";
import {AppComponent} from "./app.component";
import {UserInfoComponent} from "./component/user-info/user-info.component";
import {TransferComponent} from "./component/transfer/transfer.component";
import {ContactsComponent} from "./component/contacts/contacts.component";
import {RegistrationComponent} from "./component/registration/registration.component";

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
    path: 'register',
    component: RegistrationComponent
  },
  {
    path: 'login/callback/github',
    component: GithubCallbackComponent
  },
  {
    path: 'login/callback/google',
    component: GoogleCallbackComponent
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
