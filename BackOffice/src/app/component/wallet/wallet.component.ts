import { Component, OnInit } from '@angular/core';
import { ChtibaService } from '../../services/chtiba.service';
import { ConfirmModalComponent } from '../confirm-modal/confirm-modal.component';
import {MatDialog} from "@angular/material/dialog";

@Component({
    selector: 'app-wallet',
    templateUrl: './wallet.component.html',
    styleUrls: ['./wallet.component.css']
})
export class WalletComponent implements OnInit {
    listWallets: any[] = [];
    filteredWallets: any[] = [];
    searchStr: string = '';

    constructor(private chtibaService: ChtibaService, public dialog: MatDialog) {
    }


    ngOnInit(): void {
        this.getAllWallets();
    }

    getAllWallets() {
        this.chtibaService.getAllWallets().subscribe(res => {
            console.log(res);  // Pour inspecter les données
            this.listWallets = res;
            this.filteredWallets = res;
        });
    }

    searchWallets() {
        if (this.searchStr) {
            this.filteredWallets = this.listWallets.filter(wallet =>
                (wallet.user.firstname + ' ' + wallet.user.lastname).toLowerCase().includes(this.searchStr.toLowerCase()) ||
                wallet.user.cin.includes(this.searchStr)
            );
        } else {
            this.filteredWallets = this.listWallets; // si la barre de recherche est vide, affichez tous les portefeuilles
        }
    }

    toggleWalletStatus(id_wallet: number, isActive: boolean) {
        const action = isActive ? "désactiver" : "activer";
        const walletToToggle = this.filteredWallets.find(w => w.id_wallet === id_wallet);
        const client = walletToToggle ? `${walletToToggle.user.firstname} ${walletToToggle.user.lastname}` : "";

        const dialogRef = this.dialog.open(ConfirmModalComponent, {
            width: '400px',   // Ceci définit la largeur de la boîte de dialogue
            data: {
                action: action,
                client: client,
                mode: 'activite'

            }
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result) { // Si l'utilisateur a cliqué sur "Confirmer"
                if (isActive) {
                    this.chtibaService.deactivateWallet(id_wallet).subscribe(response => {
                        const wallet = this.listWallets.find(w => w.id_wallet === id_wallet);
                        if (wallet) wallet.isActive = false;
                    });
                } else {
                    this.chtibaService.activateWallet(id_wallet).subscribe(response => {
                        const wallet = this.listWallets.find(w => w.id_wallet === id_wallet);
                        if (wallet) wallet.isActive = true;
                    });
                }
            }
        });
    }

    deleteWallet(id_wallet: number) {
        const walletToDelete = this.filteredWallets.find(w => w.id_wallet === id_wallet);
        const client = walletToDelete ? `${walletToDelete.user.firstname} ${walletToDelete.user.lastname}` : "";

        this.dialog.open(ConfirmModalComponent, {
            width: '400px',
            data: {
                action: 'supprimer',
                client: client,
                mode: 'delete'
            }
        }).afterClosed().subscribe(confirmResult => {
            if (confirmResult) {
                this.chtibaService.deleteWallet(id_wallet).subscribe(() => {
                    this.getAllWallets();
                });
            }
        });
    }

}
