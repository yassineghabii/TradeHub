import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PredictionResult } from '../entities/prediction-result'; // Make sure to import your PredictionResult model

@Injectable({
  providedIn: 'root'
})
export class PredictService {

  private baseUrl = 'http://localhost:8080/api'; // Update with your API URL

  constructor(private http: HttpClient) { }

  createLoanApplication(creditData: any): Observable<PredictionResult> {
    const url = `${this.baseUrl}/loanApplications`;
    return this.http.post<PredictionResult>(url, creditData);
  }
}
