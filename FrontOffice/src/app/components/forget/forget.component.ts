import { Component } from '@angular/core';
import { AuthService } from '../../services/authservices';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forget',
  templateUrl: './forget.component.html',
  styleUrls: ['./forget.component.css'],
})
export class ForgetComponent {
  email: string;
  message: string;

  constructor(private authService: AuthService, private router: Router) { }

  onSubmit() {
    this.authService.forgotPassword(this.email).subscribe(
      // You may not need the response in this case since you're navigating away.
      _ => {
        this.navigateToReset();
      },
      error => {
        // Log the error but navigate anyway.
        console.error(error);
        this.navigateToReset();
      }
    );
  }

  private navigateToReset() {
    // using setTimeout to wait until the message is displayed to the user before redirecting
    setTimeout(() => this.router.navigate(['/reset']), 1000); // waits 1 second before navigation
  }
}
