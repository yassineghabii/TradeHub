import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {map, Observable} from 'rxjs';
import { Wallet } from '../Models/Wallet';
import { Card } from '../Models/Card';
import { HistoriqueChargement } from '../Models/HistoriqueChargement';
import { CardDetailsDTO } from '../Models/CardDetailsDTO';
import { ChargeResponseDTO } from '../Models/ChargeResponseDTO';

@Injectable({
    providedIn: 'root'
})
export class ChtibaService {

    private baseUrl = 'http://localhost:8080/wallets';
    private url = 'http://localhost:8080/users';

    constructor(private http: HttpClient) { }

    createWallet(userId: number, wallet: Wallet): Observable<Wallet> {
        return this.http.post<Wallet>(`${this.baseUrl}/create/${userId}`, wallet);
    }

    getAllWallets(): Observable<Wallet[]> {
        return this.http.get<Wallet[]>(`${(this.baseUrl)}/all`);
    }

    getWalletById(id: number): Observable<Wallet> {
        return this.http.get<Wallet>(`${this.baseUrl}/${id}`);
    }

    deleteWallet(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/wallets/${id}`);
    }
    deleteCard(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/cards/${id}`);
    }

    updateWallet(wallet: Wallet): Observable<Wallet> {
        return this.http.put<Wallet>(`${this.baseUrl}/update`, wallet);
    }

    getActiveWalletsForUser(userId: number): Observable<Wallet[]> {
        return this.http.get<Wallet[]>(`${this.baseUrl}/active/${userId}`);
    }

    deactivateWallet(id: number): Observable<Wallet> {
        return this.http.put<Wallet>(`${this.baseUrl}/deactivate/${id}`, {});
    }

    activateWallet(id: number): Observable<Wallet> {
        return this.http.put<Wallet>(`${this.baseUrl}/activate/${id}`, {});
    }

    getWalletBalance(id: number): Observable<number> {
        return this.http.get<number>(`${this.baseUrl}/balance/${id}`);
    }

    getWalletsByType(type: string): Observable<Wallet[]> {
        return this.http.get<Wallet[]>(`${this.baseUrl}/byType/${type}`);
    }

    getWalletsByCurrency(currency: string): Observable<Wallet[]> {
        return this.http.get<Wallet[]>(`${this.baseUrl}/byCurrency/${currency}`);
    }

    getWalletsByUserAndType(userId: number, type: string): Observable<Wallet[]> {
        return this.http.get<Wallet[]>(`${this.baseUrl}/byUserAndType/${userId}/${type}`);
    }

    convertWalletCurrency(walletId: number, targetCurrency: any): Observable<string> {
        return this.http.put<string>(`${this.baseUrl}/convert/${walletId}`, targetCurrency);
    }

    chargerPortefeuille(walletId: number, cardDetails: CardDetailsDTO): Observable<ChargeResponseDTO> {
        return this.http.post<ChargeResponseDTO>(`${this.baseUrl}/charger-portefeuille/${walletId}`, cardDetails);
    }

    createCardForUser(userId: number, card: Card): Observable<string> {
        return this.http.post<string>(`${this.baseUrl}/createCard/${userId}`, card);
    }

    getHistoriqueByUserId(userId: number): Observable<HistoriqueChargement[]> {
        return this.http.get<HistoriqueChargement[]>(`${this.baseUrl}/Historique/${userId}`);
    }

    getCardByIdWallet(walletId: number): Observable<Card> {
        return this.http.get<Card>(`${this.baseUrl}/wallet/${walletId}`);
    }

    getAllCards(): Observable<Card[]> {
        return this.http.get<Card[]>(`${this.baseUrl}/getAllCards`);
    }

    getAllHistorique(): Observable<HistoriqueChargement[]> {
        return this.http.get<HistoriqueChargement[]>(`${this.baseUrl}/getAllHistorique`);
    }

    updateCardById(cardId: number, updatedCard: Card): Observable<Card> {
        return this.http.put<Card>(`${this.baseUrl}/updateCardById/${cardId}`, updatedCard);
    }

    getUserById(userId: number): Observable<any> {
        return this.http.get<any>(`${this.url}/user/${userId}`);
    }

    updateUserById(userId: number, updatedUser: any): Observable<any> {
        return this.http.put<any>(`${this.url}/update-user/${userId}`, updatedUser);
    }

    getAllAdmins(userId: number) {
        return this.http.get<any[]>(`${this.url}/user/admins/${userId}`);
    }

    getAllPlayers(): Observable<any[]> {
        return this.http.get<any[]>(`${this.url}/clients`).pipe(
            map(players => this.mapPlayers(players))
        );
    }

    private mapPlayers(players: any[]): any[] {
        // Map the image data to base64
        return players.map(player => {
            if (player.image) {
                player.image = this.arrayBufferToBase64(player.image.data);
            }
            return player;
        });
    }

    private arrayBufferToBase64(buffer: ArrayBuffer): string {
        const binary = new Uint8Array(buffer);
        return btoa(String.fromCharCode.apply(null, binary));
    }


    addAdmin(adminDetails: any): Observable<any> {
        return this.http.post<any>(`${this.url}/user/admin`, adminDetails);
    }
    deleteUser(id: number): Observable<void> {
        return this.http.delete<void>(`${this.url}/delete-user/${id}`);
    }

}
