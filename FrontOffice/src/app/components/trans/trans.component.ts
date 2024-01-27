import { Component, OnInit } from '@angular/core';
import { MariemService } from '../../services/mariem.service';
import { Transaction } from '../../entities/Transaction';

@Component({
  selector: 'app-trans',
  templateUrl: './trans.component.html',
  styleUrls: ['./trans.component.css']
})
export class TransComponent implements OnInit {
  transactions: Transaction[] = [];

  constructor(private mariemService: MariemService) {}

  ngOnInit() {
    this.fetchTransactions();
  }

  fetchTransactions() {
    const id = this.getUserId(); // Mettez ici l'ID du client pour lequel vous voulez récupérer les transactions
    this.mariemService.getTransactionsByClientId(id)
      .subscribe((data: Transaction[]) => {
        this.transactions = data;
      });
  }
  getUserId(): number | null {
    const userIdStr = sessionStorage.getItem('id');
    return userIdStr ? Number(userIdStr) : null;
  }

}
