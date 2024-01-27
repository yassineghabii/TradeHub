  import { Component, OnInit, inject } from '@angular/core';
  import { NgxSpinnerService } from 'ngx-spinner';
  import { Observable, fromEvent, map } from 'rxjs';
  import { Router, NavigationEnd } from '@angular/router';
  import { DOCUMENT, ViewportScroller } from '@angular/common';
  import {SocialAuthService, SocialUser} from "@abacritt/angularx-social-login";
  import {AuthService} from "./services/authservices";

  @Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
  })
  export class AppComponent implements OnInit {
    title = 'vermeg';
    user: SocialUser;
    loggedIn: boolean;
    constructor(
      private spinner: NgxSpinnerService,
      private router: Router ,// Inject the Router service
      private authService: SocialAuthService,
    private customAuthService: AuthService   // Add this

  ) {}

    ngOnInit() {

      setTimeout(() => {
        /** spinner ends after 5 seconds */
        this.spinner.hide();
      }, 1000);
    }

    private readonly document = inject(DOCUMENT);
    private readonly viewport = inject(ViewportScroller);

    readonly showScroll$: Observable<boolean> = fromEvent(
      this.document,
      'scroll'
    ).pipe(map(() => this.viewport.getScrollPosition()?.[1] > 0));

    onScrollToTop(): void {
      this.viewport.scrollToPosition([0, 0]);
    }
    shouldShowHeaderAndFooter(): boolean {
      const excludedRoutes = ['login', 'inscription', 'reset', 'forget'];
      const currentRoute = this.router.routerState.snapshot.url.split('/')[1];
      return !excludedRoutes.includes(currentRoute);
    }

  }
