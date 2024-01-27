import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const token = this.getToken();

    // Ideally, remove this log or make it conditional based on the environment
    console.log("token:", token);

    if (token) {
      return true;
    }

    this.router.navigate(['/login']);
    return false;
  }

  private getToken(): string | null {
    return this.cleanToken(sessionStorage.getItem('token'));
  }

  private cleanToken(token: string | null): string | null {
    return token ? token.replace(/"/g, '') : null;
  }
}
