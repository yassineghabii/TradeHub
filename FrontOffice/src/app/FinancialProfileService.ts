import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FinancialProfile } from './entities/financial-profile';

@Injectable({
  providedIn: 'root'
})
export class FinancialProfileService {
    private baseUrl = 'http://localhost:8089/financial-profiles'; // Remplacez par l'URL de votre API Spring Boot

    constructor(private http: HttpClient) {}

    addFinancialProfile(financialProfile: FinancialProfile): Observable<FinancialProfile> {
        return this.http.post<FinancialProfile>(`${this.baseUrl}/add`, financialProfile);
    }

    updateFinancialProfile(id: number, financialProfile: FinancialProfile): Observable<FinancialProfile> {
        return this.http.put<FinancialProfile>(`${this.baseUrl}/update/${id}`, financialProfile);
    }

saveOrUpdateFinancialProfile(clientId: number, financialProfile: FinancialProfile): Observable<FinancialProfile> {
  return this.http.post<FinancialProfile>(`${this.baseUrl}/save-or-update/${clientId}`, financialProfile);
}
getFinancialProfileByClientId(clientId: number): Observable<FinancialProfile> {
  return this.http.get<FinancialProfile>(`${this.baseUrl}/profile/${clientId}`);
}

}