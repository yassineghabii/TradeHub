import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ReclamationService {
  private apiUrl = 'http://localhost:8083/pi_back/reclamations'; // Your backend API URL

  constructor(private http: HttpClient) { }

  getAllReclamations() {
    return this.http.get<any[]>(this.apiUrl);
  }

  addReclamation(reclamation: any) {
    return this.http.post(this.apiUrl, reclamation);
  }

  updateReclamation(id: number, reclamation: any) {
    return this.http.put(`${this.apiUrl}/${id}`, reclamation);
  }

  deleteReclamation(id: number) {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }

  getProcessedReclamations() {
    return this.http.get<any[]>(`${this.apiUrl}/processed`);
  }

  getPendingReclamations() {
    return this.http.get<any[]>(`${this.apiUrl}/pending`);
  }

  archiveResolvedReclamations() {
    return this.http.post<any[]>(`${this.apiUrl}/archive/resolved`, {});
  }

  followReclamationStatus(reclamationId: number) {
    return this.http.get<any>(`${this.apiUrl}/follow/${reclamationId}`);
  }
}
