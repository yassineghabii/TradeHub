// src/app/nom-du-service.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FlaskService {
  private apiUrl = 'http://localhost:5002/actua'; // Mettez à jour l'URL avec votre route Flask

  constructor(private http: HttpClient) {}

  lancerScript(): Observable<any> {
    return this.http.get(this.apiUrl);
  }
}
