import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {LoginService} from "../../../../core/services/login.service";

@Component({
  selector: 'app-google-callback',
  templateUrl: './google-callback.component.html',
  styleUrls: ['./google-callback.component.scss']
})
export class GoogleCallbackComponent implements OnInit {
  constructor(
    private router: Router,
    private loginService: LoginService,
    private route: ActivatedRoute,) {}

  ngOnInit(): void {
    let code: string | null = this.route.snapshot.queryParamMap.get('code');
    let state: string | null = this.route.snapshot.queryParamMap.get('state');
    let scope: string | null = this.route.snapshot.queryParamMap.get('scope');
    let authuser: string | null = this.route.snapshot.queryParamMap.get('authuser');
    let prompt: string | null = this.route.snapshot.queryParamMap.get('prompt');

    console.log("SCOPEEE : ", scope);

    let codeClean = code?.replaceAll('://', '%3A%2F%2F');
    let stateClean = state?.replaceAll('://', '%3A%2F%2F');
    let scopeClean = scope?.replaceAll('://', '%3A%2F%2F');
    let authuserClean = authuser?.replaceAll('://', '%3A%2F%2F');
    let promptClean = prompt?.replaceAll('://', '%3A%2F%2F');

    codeClean = codeClean?.replaceAll("/", '%2F');
    stateClean = stateClean?.replaceAll("/", '%2F');
    scopeClean = scopeClean?.replaceAll("/", '%2F');
    authuserClean = authuserClean ?.replaceAll("/", '%2F');
    promptClean = promptClean ?.replaceAll("/", '%2F');

    scopeClean = scopeClean?.replaceAll(" ", "+");


    const stateCalc = stateClean?.substring(0, stateClean?.length -1) + "%3D";
    if(codeClean && stateClean && scopeClean && authuserClean && promptClean) {

      this.loginService.fetchTokenGoogle(codeClean, stateCalc, scopeClean, authuserClean, promptClean)
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
