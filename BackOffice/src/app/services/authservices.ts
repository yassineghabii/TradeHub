import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Observable, tap} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private BASE_URL = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) { }

  login(loginRequest: any): Observable<any> {
    const transformedLoginRequest = {
      id_admin: loginRequest.id_admin,  // Assurez-vous que cela correspond au champ de votre formulaire
      pwd_user: loginRequest.password  // Assurez-vous que cela correspond au champ de votre formulaire
    };

    return this.http.post(`${this.BASE_URL}/loginAdmin`, transformedLoginRequest)
        .pipe(
            tap(response => {
              console.log('Réponse après connexion réussie:', response);
              sessionStorage.setItem('id', response.id);
              sessionStorage.setItem('token', response.token);
              sessionStorage.setItem('role', response.role);
              sessionStorage.setItem('firstname', response.firstname);
              sessionStorage.setItem('email', response.email);
              sessionStorage.setItem('lastname', response.lastname);
              sessionStorage.setItem('idAdmin', response.id_admin);
            })
        );
  }

  register(registerRequest: any): Observable<any> {
    return this.http.post(`${this.BASE_URL}/register`, registerRequest);
  }

  logout(id: number): Observable<any> {
    return this.http.delete(`${this.BASE_URL}/logout/${id}`, {});
  }


  forgotPassword(email: string): Observable<any> {
    return this.http.post(`${this.BASE_URL}/forgot-password`, email, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      }),
      responseType: 'text' // <-- This line is crucial
    });
  }

  resetPassword(token: string, newPassword: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return  this.http.post(`${this.BASE_URL}/reset-password?token=${token}`, newPassword, { headers, responseType: 'text' });

  }
  getToken(): string | null {
    const token = sessionStorage.getItem('token');
    const cleanedToken = token ? token.replace(/"/g, '') : null; // remove double quotes if they exist
    return token ? token.replace(/"/g, '') : null; // remove double quotes if they exist
    console.log("token:", cleanedToken); // log the role
    return cleanedToken;

  }
  getRole(): string | null {
    const role = sessionStorage.getItem('role');
    const cleanedRole = role ? role.replace(/"/g, '') : null; // remove double quotes if they exist
    return cleanedRole;
  }

  getUserId(): number | null {
    const userIdStr = sessionStorage.getItem('id');
    return userIdStr ? Number(userIdStr) : null;
  }
  getName(): string | null {
    // Check if we've already logged the name
    const hasLoggedName = sessionStorage.getItem('hasLoggedName');

    const name = sessionStorage.getItem('full_name');
    const cleanedName = name ? name.replace(/"/g, '') : null; // remove double quotes if they exist

    if (!hasLoggedName) {
      console.log("full_name:", cleanedName); // log the role
      sessionStorage.setItem('hasLoggedName', 'true'); // Set the flag in session storage
    }

    return cleanedName;
  }

}

