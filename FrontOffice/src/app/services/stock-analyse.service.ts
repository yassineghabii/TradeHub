// stock-prediction.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StockAnalyseService {

  private apiUrl = 'http://localhost:8501';  // Update with your Flask API URL

  constructor(private http: HttpClient) { }


  // Add more methods for other API calls as needed
}
