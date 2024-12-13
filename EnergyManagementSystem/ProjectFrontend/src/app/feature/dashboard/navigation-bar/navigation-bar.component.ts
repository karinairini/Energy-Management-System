import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {getUser} from "../../../core/service/user/user.service";

@Component({
  selector: 'app-navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrl: './navigation-bar.component.scss'
})
export class NavigationBarComponent {

  loggedUser = getUser();

  constructor(private router: Router) {
  }

  goToClients(): void {
    this.router.navigateByUrl("/dashboard/clients");
  }

  goToDevices(): void {
    this.router.navigateByUrl("/dashboard/devices");
  }

  goToChat(): void {
    this.router.navigateByUrl("/dashboard/chat");
  }

  logOut(): void {
    this.clearCookies();
    localStorage.removeItem('loggedUser');
    this.router.navigateByUrl('/authentication/login');
  }

  private clearCookies(): void {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
      const cookie = cookies[i];
      const equalPos = cookie.indexOf('=');
      const name = equalPos > -1 ? cookie.slice(0, equalPos) : cookie;
      document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:00 GMT;path=/;';
    }
  }
}
