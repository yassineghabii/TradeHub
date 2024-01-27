import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ChatResponse } from "./entities/ChatResponse";

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {

  private baseUrl: string = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  chatWithPython(message: string): Observable<ChatResponse> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    const body = {
      message: message
    };

    return this.http.post<ChatResponse>(`${this.baseUrl}/chat`, body, { headers: headers });
  }

  // Nouvelle méthode pour l'envoi de fichiers
  uploadFileToPython(file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('csv-file', file, file.name);

    return this.http.post(`${this.baseUrl}/upload`, formData);  // Pas besoin de spécifier les headers ici, Angular gère le content-type multipart/form-data automatiquement avec FormData
  }

  resetChatbot(): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/reset-chatbot`, null);
  }
  
}
