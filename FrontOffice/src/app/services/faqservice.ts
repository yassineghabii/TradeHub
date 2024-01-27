// faq.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FAQ } from '../entities/faq';

@Injectable({
    providedIn: 'root',
})
export class FAQService {
  private apiUrl = 'http://localhost:8080/api/faqs'; // Update with your Spring Boot API URL

  constructor(private http: HttpClient) {}

  getFAQs(): Observable<FAQ[]> {
    return this.http.get<FAQ[]>(this.apiUrl);
      }
      getFAQById(id: number): Observable<FAQ> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.get<FAQ>(url);
  }
}
