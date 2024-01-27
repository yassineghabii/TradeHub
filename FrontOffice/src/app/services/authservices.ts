import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from '@angular/common/http';
import {catchError, map, Observable, tap, throwError} from 'rxjs';
import {SocialUser} from "@abacritt/angularx-social-login";
import {RegisterRequest} from "../entities/registerRequest";
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private BASE_URL = 'http://localhost:8080/auth';
  private BASE_URLL = 'http://localhost:8080/users';

  constructor(private http: HttpClient) {
  }

  login(loginRequest: any): Observable<any> {
    const transformedLoginRequest = {
      email: loginRequest.email,
      pwd_user: loginRequest.password
    };
    return this.http.post(`${this.BASE_URL}/login`, transformedLoginRequest)
      .pipe(
        tap(response => {
          sessionStorage.setItem('id', response.id);
          sessionStorage.setItem('token', response.token);
          sessionStorage.setItem('role', response.role);
          sessionStorage.setItem('full_name', response.full_name);
          sessionStorage.setItem('firstname', response.firstname);
          sessionStorage.setItem('lastname', response.lastname);
          sessionStorage.setItem('id_wallet', response.id_wallet);
          sessionStorage.setItem('id_card', response.id_card);

        })
      );
  }

  register(profileImage: File, request: any) {
    const formData = new FormData();
    formData.append('profileImage', profileImage);
    formData.append('email', request.email);
    formData.append('pwd_user', request.pwd_user);
    formData.append('firstname', request.firstname);
    formData.append('lastname', request.lastname);
    formData.append('cin', request.cin);
    formData.append('address', request.address);
    formData.append('phonenumber', request.phonenumber);
    formData.append('type', request.type);



    return this.http.post(`${this.BASE_URL}/register`, formData);
  }

  logout(id: number): Observable<any> {
    return this.http.delete(`${this.BASE_URL}/logout/${id}`);
  }

  private BASE_URL1 = 'http://localhost:8080/auth/request-password-reset';

  forgotPassword(identifier: string): Observable<string> {
    let params = new HttpParams().set('identifier', identifier.trim());

    // Make the POST request
    return this.http.post<string>(this.BASE_URL1, null, { params });
  }


  resetPassword(token: string, newPassword: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    return this.http.post(`${this.BASE_URL}/reset-password?token=${token}`, newPassword, {
      headers,
      responseType: 'text'
    });

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
  getCardId(): number | null {
    const cardIdStr = sessionStorage.getItem('id_card');
    return cardIdStr ? Number(cardIdStr) : null;
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

  private apiUrl = 'http://localhost:8080/auth/google-login';
  private fb = 'http://localhost:8080/auth/fb-login';
  private m = 'http://localhost:8080/auth/microsoft-login';
  private a = 'http://localhost:8080/auth/amazon-login';

  sendGoogleUserToBackend(socialUser: SocialUser) {
    console.log('Sending this Google user to backend:', socialUser);
    return this.http.post(this.apiUrl, socialUser).pipe(
      tap((response: any) => {
        if (response.id) sessionStorage.setItem('id', response.id.toString());
        if (response.token) sessionStorage.setItem('token', response.token);
        if (response.role) sessionStorage.setItem('role', response.role);
        if (response.full_name) sessionStorage.setItem('full_name', response.full_name);
        if (response.firstname) sessionStorage.setItem('firstname', response.firstname);
        if (response.lastname) sessionStorage.setItem('lastname', response.lastname);
        if (response.id_wallet) sessionStorage.setItem('id_wallet', response.id_wallet.toString());
        if (response.id_card) sessionStorage.setItem('id_card', response.id_card.toString());

        console.log('Google user login data stored in session storage');
      })
    );
  }
  sendFaceUserToBackend(socialUser: SocialUser) {
    console.log('Sending this Facebook user to backend:', socialUser);
    return this.http.post(this.fb, socialUser).pipe(
      tap((response: any) => {
        if (response.id) sessionStorage.setItem('id', response.id.toString());
        if (response.token) sessionStorage.setItem('token', response.token);
        if (response.role) sessionStorage.setItem('role', response.role);
        if (response.firstname) sessionStorage.setItem('firstname', response.firstname);
        if (response.lastname) sessionStorage.setItem('lastname', response.lastname);
        if (response.id_wallet) sessionStorage.setItem('id_wallet', response.id_wallet.toString());
        if (response.id_card) sessionStorage.setItem('id_card', response.id_card.toString());
        if (response.full_name) sessionStorage.setItem('full_name', response.full_name);

        console.log('Facebook user login data stored in session storage');
      })
    );
  }
  sendMUserToBackend(socialUser: SocialUser) {
    console.log('Sending this Microsoft user to backend:', socialUser);
    return this.http.post(this.m, socialUser).pipe(
      tap((response: any) => {
        if (response.id) sessionStorage.setItem('id', response.id.toString());
        if (response.token) sessionStorage.setItem('token', response.token);
        if (response.role) sessionStorage.setItem('role', response.role);
        if (response.firstname) sessionStorage.setItem('firstname', response.firstname);
        if (response.lastname) sessionStorage.setItem('lastname', response.lastname);
        if (response.id_wallet) sessionStorage.setItem('id_wallet', response.id_wallet.toString());
        if (response.id_card) sessionStorage.setItem('id_card', response.id_card.toString());
        if (response.full_name) sessionStorage.setItem('full_name', response.full_name);

        console.log('Facebook user login data stored in session storage');
      })
    );
  }
  sendAUserToBackend(socialUser: SocialUser) {
    console.log('Sending this Amazp, user to backend:', socialUser);
    return this.http.post(this.a, socialUser).pipe(
      tap((response: any) => {
        if (response.id) sessionStorage.setItem('id', response.id.toString());
        if (response.token) {
          sessionStorage.setItem('token', response.token);
        } else {
          console.error('Token is null, something went wrong.');
          // Gérer l'erreur comme il convient, par exemple en avertissant l'utilisateur
        }
        if (response.role) sessionStorage.setItem('role', response.role);
        if (response.firstname) sessionStorage.setItem('firstname', response.firstname);
        if (response.lastname) sessionStorage.setItem('lastname', response.lastname);
        if (response.id_wallet) sessionStorage.setItem('id_wallet', response.id_wallet.toString());
        if (response.id_card) sessionStorage.setItem('id_card', response.id_card.toString());
        if (response.cin) sessionStorage.setItem('cin', response.cin);
        if (response.full_name) sessionStorage.setItem('full_name', response.full_name);

        console.log('Amazon user login data stored in session storage');
      })
    );
  }

  updateClient(id: number, chosenPwd: string, profileImage: File, cin: string, phone_number: string, address: string, role: string, type: string): Observable<any> {
    const formData = new FormData();
    formData.append('pwd_user', chosenPwd);
    formData.append('type', type);

    if (profileImage) {
      formData.append('profile_image', profileImage, profileImage.name);
    }

    formData.append('cin', cin);
    formData.append('phone_number', phone_number);
    formData.append('address', address);
    formData.append('role', role);

    return this.http.put(`${this.BASE_URL}/update/${id}`, formData).pipe(
      map(response => {
        // Handle the successful response here
        if (response && response.hasOwnProperty('authResponse')) {
          const authResponse = response['authResponse'];

          // Save id_wallet and id_card into session storage
          if (authResponse.id_wallet) {
            sessionStorage.setItem('id_wallet', JSON.stringify(authResponse.id_wallet));
          }
          if (authResponse.id_card) {
            sessionStorage.setItem('id_card', JSON.stringify(authResponse.id_card));
          }
        }
        return response; // This will allow the caller to also handle the response
      }),
      catchError((error: any) => {
        // Handle the error here
        console.error('Erreur lors de la mise à jour du client :', error);
        // Propagate the error to the caller
        return throwError(() => new Error('Erreur lors de la mise à jour du client'));
      })
    );
  }
  hasPasswordAndImage(clientId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.BASE_URL}/hasPasswordAndImage/${clientId}`);
  }
  private url = 'http://localhost:8080/users';

  getClientProfile(clientId: number): Observable<any> {
    const url = `${this.url}/${clientId}/profile`; // Construct the URL

    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    // Handle the HTTP error here
    let errorMsg: string;
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred
      errorMsg = `An error occurred: ${error.error.message}`;
    } else {
      // The backend returned an unsuccessful response code
      errorMsg = `Server returned code: ${error.status}, error message is: ${error.message}`;
    }
    console.error(errorMsg);
    return throwError(errorMsg);
  }
  changePassword(userId: number, oldPassword: string, newPassword: string) {
    const requestBody = {
      oldPassword,
      newPassword
    };
    return this.http.post(`${this.url}/${userId}/change`, requestBody, { responseType: 'text' });
  }

  getAllUsers(){
    return this.http.get(`${this.BASE_URLL}/getAllUsers`)
  }
  getUser(IdUser : any){
    return  this.http.get(`${this.BASE_URLL}/getUser/${IdUser}`)
  }

}
