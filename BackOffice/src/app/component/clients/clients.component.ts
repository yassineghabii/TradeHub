import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import { ChtibaService } from '../../services/chtiba.service';
import { ConfirmModalComponent } from '../confirm-modal/confirm-modal.component';
import { MatDialog } from "@angular/material/dialog";
import { Client } from "../../Models/Client";
import {DomSanitizer, SafeUrl} from '@angular/platform-browser';
import {MatPaginator} from "@angular/material/paginator";

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.scss']
})
export class ClientsComponent implements OnInit,AfterViewInit  {
  listPlayers: Client[] = [];
  filteredPlayers: Client[] = [];
  searchStr: string = '';
  selectedPlayer: Client;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private chtibaService: ChtibaService, public dialog: MatDialog, private sanitizer: DomSanitizer) {}

  ngOnInit(): void {
    this.getAllPlayers();
  }
  // Method to sanitize blob URLs
  sanitizeBlobUrl(yourBlobData: string): SafeUrl {
    const blobUrl = this.sanitizer.bypassSecurityTrustUrl('unsafe:blob:' + yourBlobData);
    return blobUrl;
  }
  ngAfterViewInit() {
    this.paginator.page.subscribe(() => {
      this.filteredPlayers = this.paginatePlayers();
    });
  }

  private paginatePlayers() {
    const startIndex = this.paginator.pageIndex * this.paginator.pageSize;
    return this.listPlayers.slice(startIndex, startIndex + this.paginator.pageSize);
  }


  getAllPlayers() {
    this.chtibaService.getAllPlayers().subscribe(players => {
      console.log('Données d\'image reçues :', players);

      this.listPlayers = players;

      this.filteredPlayers = [...this.listPlayers];
    });
  }

  searchPlayers() {
    if (this.searchStr.trim()) {
      this.filteredPlayers = this.listPlayers.filter(player =>
          (player.firstname + ' ' + player.lastname).toLowerCase().includes(this.searchStr.toLowerCase())
      );
    } else {
      this.filteredPlayers = [...this.listPlayers];  // Remettez la liste filtrée à la liste originale
    }
  }

  openUpdateModal(admin: Client) {
    const dialogRef = this.dialog.open(ConfirmModalComponent, {
      width: '400px',
      data: {
        action: 'mettre à jour',
        client: `${admin.firstname} ${admin.lastname}`,
        mode: 'update',
        admin: admin  // On passe l'objet admin au dialogue
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Vous avez les modifications dans result
        this.chtibaService.updateUserById(admin.id, result).subscribe(updatedAdmin => {
          this.getAllPlayers();
        });
      }
    });
  }
  dataURItoBlob(dataURI: string) {
    const byteString = atob(dataURI.split(',')[1]);
    const mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }
    const blob = new Blob([ab], { type: mimeString });
    return URL.createObjectURL(blob);
  }

  deletePlayer(playerId: number) {
    const playerToDelete = this.filteredPlayers.find(player => player.id === playerId);
    const playerName = playerToDelete ? `${playerToDelete.firstname} ${playerToDelete.lastname}` : "";

    this.dialog.open(ConfirmModalComponent, {
      width: '400px',
      data: {
        action: 'supprimer',
        client: playerName,
        mode: 'delete'
      }
    }).afterClosed().subscribe(confirmResult => {
      if (confirmResult) {
        this.chtibaService.deleteUser(playerId).subscribe(() => {
          this.getAllPlayers();  // Recharger la liste après la suppression
        });
      }
    });
  }
}
