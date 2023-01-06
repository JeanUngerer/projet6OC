import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./component/login/login.component";

const routes: Routes = [
  {path: 'login', component: LoginComponent}
];

/*
const routes: Routes = [
  {path: 'devices', component: DevicesComponent},
  {path: 'compute-key', component: ComputeKeyComponent},
  {path: 'device/:deviceId', component: DeviceDetailComponent},
  {path: '', redirectTo: '/devices', pathMatch: 'full'},
  {path: '**', redirectTo: '/devices'},
];*/
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
