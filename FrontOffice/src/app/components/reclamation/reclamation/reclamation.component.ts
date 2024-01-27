// reclamation.component.ts

import { Component, OnInit } from '@angular/core';
import { Reclamation, StatusReclamation } from 'src/app/entities/Reclamation';
import { ReclamationService } from 'src/app/services/reclamationservice'; 
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-reclamation',
  templateUrl: './reclamation.component.html',
  styleUrls: ['./reclamation.component.css'],
})
export class ReclamationComponent implements OnInit {
  reclamationForm: FormGroup;
  reclamations: Reclamation[] = [];
  newReclamation: Reclamation = {
    description: '',
    status: StatusReclamation.NEW,
    archived: false,
    date: new Date(),
  };



  constructor(private fb: FormBuilder, private reclamationService: ReclamationService) {
    // Initialize the form group
    this.reclamationForm = this.fb.group({
      date: [this.newReclamation.date, Validators.required],
      email: [this.newReclamation.email, [Validators.required, Validators.email]],
      description: [this.newReclamation.description, Validators.required],
      status: [this.newReclamation.status],
      archived: [this.newReclamation.archived],
    });
  }

  ngOnInit(): void {
    this.loadReclamations();
  }

  loadReclamations(): void {
    this.reclamationService.getAllReclamations().subscribe(
      (data) => {
        this.reclamations = data;
      },
      (error) => {
        console.error('Error loading reclamations:', error);
      }
    );
  }

  addReclamation(): void {
    if (this.reclamationForm.valid) {
      const reclamationData = this.reclamationForm.value;
      this.reclamationService.addReclamation(reclamationData).subscribe(
        (data) => {
          this.reclamations.push(data);
          this.resetForm();
        },
        (error) => {
          console.error('Error adding reclamation:', error);
        }
      );
    }
  }

  updateReclamation(id: number, updatedReclamation: { status: string }): void {
    // Convert the status string to StatusReclamation enum
    const status: StatusReclamation = updatedReclamation.status as StatusReclamation;

    this.reclamationService.updateReclamation(id, { status }).subscribe(
      (data) => {
        const index = this.reclamations.findIndex((r) => r.id === id);
        if (index !== -1) {
          this.reclamations[index] = data;
        }
      },
      (error) => {
        console.error('Error updating reclamation:', error);
      }
    );
  }


  deleteReclamation(id: number): void {
    this.reclamationService.deleteReclamation(id).subscribe(
      () => {
        this.reclamations = this.reclamations.filter((r) => r.id !== id);
      },
      (error) => {
        console.error('Error deleting reclamation:', error);
      }
    );
  }

  getCurrentDate(): string {
    const today = new Date();
    const year = today.getFullYear();
    const month = today.getMonth() + 1; // months are zero-based
    const day = today.getDate();

    return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;
  }

  resetForm(): void {
    this.reclamationForm.reset({
      date: this.getCurrentDate(),
      email: '',
      description: '',
      status: StatusReclamation.NEW,
      archived: false,
    });
  }
}
