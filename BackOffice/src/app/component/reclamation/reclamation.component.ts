import { Component, OnInit } from '@angular/core';
import { ReclamationService } from 'app/services/reclamation.service';
import { Reclamation } from 'app/Models/Reclamation'

@Component({
  selector: 'app-reclamation',
  templateUrl: './reclamation.component.html',
  styleUrls: ['./reclamation.component.scss']
})
export class ReclamationComponent implements OnInit {
  reclamations: Reclamation[] = [];
  filteredReclamations: Reclamation[] = []; // Add this line
  showProcessedReclamations: boolean = false;
  processedReclamations: Reclamation[] = [];
  pendingReclamations: Reclamation[] = [];
  showPendingReclamations: boolean = false;

  constructor(private reclamationService: ReclamationService) { }

  ngOnInit(): void {
    this.loadReclamations();
  }

  loadReclamations() {
    this.reclamationService.getAllReclamations().subscribe((data: Reclamation[]) => {
      this.reclamations = data;
      this.filteredReclamations = data; // Set filteredReclamations initially
    });

    this.reclamationService.getProcessedReclamations().subscribe((data: Reclamation[]) => {
      this.processedReclamations = data;
    });

    this.reclamationService.getPendingReclamations().subscribe((data: Reclamation[]) => {
      this.pendingReclamations = data;
    });
  }
  addReclamation() {
    const newReclamation = { /* Your reclamation data here */ };
    this.reclamationService.addReclamation(newReclamation).subscribe(() => {
      this.loadReclamations(); // Refresh data
    });
  }

  updateReclamation(id: number, updatedReclamation: any) {
    this.reclamationService.updateReclamation(id, updatedReclamation).subscribe(() => {
      this.loadReclamations(); // Refresh data
    });
  }

  deleteReclamation(id: number) {
    this.reclamationService.deleteReclamation(id).subscribe(() => {
      this.loadReclamations(); // Refresh data
    });
  }

  toggleReclamations() {
    this.showProcessedReclamations = !this.showProcessedReclamations;
    if (this.showProcessedReclamations) {
      this.loadProcessedReclamations();
    } else {
      this.loadReclamations();
    }
  }


  togglePendingReclamations() {
    this.showPendingReclamations = !this.showPendingReclamations;
  }
  loadProcessedReclamations() {
    this.reclamationService.getProcessedReclamations().subscribe((data: any[]) => {
      this.processedReclamations = data;
    });
  }

  loadPendingReclamations() {
    this.reclamationService.getPendingReclamations().subscribe((data: any[]) => {
      this.pendingReclamations = data;
    });
  }

  resetReclamations() {
    this.showProcessedReclamations = false;
    this.showPendingReclamations = false;
    this.filteredReclamations = this.reclamations; // Reset filteredReclamations to the original array
  }
}
