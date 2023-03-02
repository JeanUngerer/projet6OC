import {Component, OnInit} from '@angular/core';
import {LoginService} from "../../../../core/services/login.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-github-callback',
  templateUrl: './github-callback.component.html',
  styleUrls: ['./github-callback.component.scss']
})
export class GithubCallbackComponent implements OnInit {

  constructor(
    private router: Router,
    private loginService: LoginService,
    private route: ActivatedRoute,) {}

  ngOnInit(): void {
    const code: string | null = this.route.snapshot.queryParamMap.get('code');
    const state: string | null = this.route.snapshot.queryParamMap.get('state');


    const stateCalc = state?.substring(0, state?.length-1) + "%3D";
    if(code && stateCalc) {

      this.loginService.fetchTokenGithub(code, stateCalc)
        .subscribe({
            next: (r) => {
              console.log("RESULT : ", r);
              this.handleAuthSuccess()
            },
            error: (err) => this.handleAuthError(err)
          }
        );
    }
  }


  /**
   * If login and password match, access to the site
   */
  handleAuthSuccess() {
    this.router.navigate(['home']);
  }

  /**
   * else display error to the user
   * @param err
   */
  handleAuthError(err: any) {
    this.router.navigate(['login']);
    console.log("LOG ERROR : ", err);
  }

}
