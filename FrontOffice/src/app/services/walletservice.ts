import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {HistoriqueChargement} from "../entities/HistoriqueChargement";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Card} from "../entities/Card";
import {Portfolio} from "../entities/Portfolio";

@Injectable({
  providedIn: 'root'
})
export class WalletService {
  private apiUrl = 'http://localhost:8080/wallets';

  constructor(private http: HttpClient) { }
  getPortfolioById(id: number): Observable<Portfolio> {
    const url = `${this.apiUrl}/${id}/Portfolio`;
    return this.http.get<Portfolio>(url);
  }

  getWalletById(id_wallet: number): Observable<any> {
    const url = `${this.apiUrl}/${id_wallet}`;
    return this.http.get(url);
  }
  deleteWallet(id: number): Observable<void> {
    const url = `${this.apiUrl}/wallets/${id}`;
    return this.http.delete<void>(url);
  }
  deactivateWallet(id: number): Observable<any> {
    const url = `${this.apiUrl}/deactivate/${id}`;
    return this.http.put(url, null);
  }

  activateWallet(id: number): Observable<any> {
    const url = `${this.apiUrl}/activate/${id}`;
    return this.http.put(url, null);
  }
  convertWalletCurrency(id: number, targetCurrency: string): Observable<any> {
    const url = `${this.apiUrl}/convert/${id}`;
    const requestBody = { targetCurrency: targetCurrency };
    return this.http.put(url, requestBody);
  }
  chargeWallet(walletId: number, cardDetails: any): Observable<any> {
    const url = `${this.apiUrl}/charger-portefeuille/${walletId}`;
    return this.http.post(url, cardDetails);
  }
  convertirDevise(montant: number, deviseSource: string, deviseCible: string): Observable<any> {
    const url = `${this.apiUrl}/convertCurrency`;
    const requestBody = { amount: montant, fromCurrencyCode: deviseSource, toCurrencyCode: deviseCible };
    return this.http.post(url, requestBody);
  }
  getHistoriqueByUserId(userId: number): Observable<HistoriqueChargement[]> {
    const url = `${this.apiUrl}/Historique/${userId}`;
    return this.http.get<HistoriqueChargement[]>(url);
  }
  createCardForUser(userId: number, card: any): Observable<any> {
    const url = `${this.apiUrl}/createCard/${userId}`; // L'URL complète pour l'appel POST
    return this.http.post(url, card);
  }
  getCardByIdWallet(walletId: number): Observable<Card> {
    const url = `${this.apiUrl}/wallet/${walletId}`; // L'URL complète pour l'appel GET
    return this.http.get<Card>(url);
  }

}

