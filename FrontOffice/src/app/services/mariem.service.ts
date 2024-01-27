import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, Observable, tap} from 'rxjs';
import {Titre} from "../entities/Titre";
import {LigneCarnet} from "../entities/LigneCarnet";
import {SymboleDTO} from "../entities/SymboleDTO";
import {OrdreAchatLimiteDTO} from "../entities/OrdreAchatLimiteDTO";
import {OrdreVenteLimiteDTO} from "../entities/OrdreVenteLimiteDTO";
import {OrdreAchatAuMarcheDTO} from "../entities/OrdreAchatAuMarcheDTO";
import {OrdreVenteAuMarcheDTO} from "../entities/OrdreVenteAuMarcheDTO";
import firebase from "firebase/compat";
import { Transaction } from '../entities/Transaction';



@Injectable({
  providedIn: 'root'
})
export class MariemService {
  private baseUrl = 'http://localhost:8080/mariem';



  constructor(private http: HttpClient) {}
  annulerOrdreA(idOrdre: number): Observable<any> {
    const url = `${this.baseUrl}/annuler-ordrea/${idOrdre}`;
    return this.http.put(url, {});
  }

  annulerOrdreV(idOrdre: number): Observable<any> {
    const url = `${this.baseUrl}/annuler-ordrev/${idOrdre}`;
    return this.http.put(url, {});
  }

  getTransactionsByClientId(clientId: number): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.baseUrl}/transaction/${clientId}`);
  }

  getOrdresAchatLimiteByClientId(clientId: number): Observable<OrdreAchatLimiteDTO[]> {
    return this.http.get<OrdreAchatLimiteDTO[]>(`${this.baseUrl}/client/${clientId}/achat/limite`);
  }

  // Méthode pour récupérer les ordres de vente limite par clientId
  getOrdresVenteLimiteByClientId(clientId: number): Observable<OrdreVenteLimiteDTO[]> {
    return this.http.get<OrdreVenteLimiteDTO[]>(`${this.baseUrl}/client/${clientId}/vente/limite`);
  }

  // Méthode pour récupérer les ordres d'achat au marché par clientId
  getOrdresAchatAuMarcheByClientId(clientId: number): Observable<OrdreAchatAuMarcheDTO[]> {
    return this.http.get<OrdreAchatAuMarcheDTO[]>(`${this.baseUrl}/client/${clientId}/achat/aumarche`);
  }

  // Méthode pour récupérer les ordres de vente au marché par clientId
  getOrdresVenteAuMarcheByClientId(clientId: number): Observable<OrdreVenteAuMarcheDTO[]> {
    return this.http.get<OrdreVenteAuMarcheDTO[]>(`${this.baseUrl}/client/${clientId}/vente/aumarche`);
  }

  genererCarnetOrdres(idTitre: number): Observable<any> {
    const url = `${this.baseUrl}/genererCarnet/${idTitre}`;
    return this.http.get(url);
  }
  obtenirSymboleParIdTitre(idTitre: number): Observable<SymboleDTO> {
    const url = `${this.baseUrl}/${idTitre}/symbole`;
    return this.http.get<SymboleDTO>(url);
  }

  passerOrdreAchat(idTitre: number, ordre: any,idClient : number): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(`${this.baseUrl}/achat/${idTitre}/${idClient}`, ordre, { headers: headers });
  }

  passerOrdreVente(idTitre: number, ordre: any,idClient : number): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(`${this.baseUrl}/vente/${idTitre}/${idClient}`, ordre, { headers: headers });
  }


  ajouterTitre(titreRequest: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/ajouterTitre`, titreRequest);
  }

  afficherToutesLesDonnees(interval: string): Observable<string> {
    return this.http.get<string>(`${this.baseUrl}/afficher-toutes-les-donnees?interval=${interval}`);
  }

  afficherToutesLesDonneesPourUnSymbole(symbol: string, interval: string): Observable<string> {
    return this.http.get<string>(`${this.baseUrl}/afficher-toutes-les-donnees/${symbol}/${interval}`);
  }

  getAllOrdresA(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/all-ordreAchat`);
  }

  annulerOrdre(idOrdre: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/annuler-ordre/${idOrdre}`);
  }

  getAllTransactions(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/all-transactions`);
  }
  getAllTitres(): Observable<Titre[]> {
    return this.http.get<Titre[]>(`${this.baseUrl}/AllTitres`);
  }
  getCarnetOrdres(idTitre: number): Observable<LigneCarnet[]> {
    const url = `${this.baseUrl}/carnet/${idTitre}`;
    return this.http.get<LigneCarnet[]>(url).pipe(
      tap((response: LigneCarnet[]) => {
        console.log('Réponse de getCarnetOrdres:', response);
      }),
    );
  }

}
