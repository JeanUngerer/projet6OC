import { Component } from '@angular/core';
import { AuthService} from "../../../core/services/auth.service";

@Component({
  selector: 'app-top-nav',
  templateUrl: './top-nav.component.html',
  styleUrls: ['./top-nav.component.scss']
})
export class TopNavComponent {

  constructor(

    private authService: AuthService,
  ) {}

  handleClickLogout() {
    this.authService.logout();
  }

}
