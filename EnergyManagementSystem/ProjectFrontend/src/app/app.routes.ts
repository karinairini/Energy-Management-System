import {Routes} from '@angular/router';
import {authGuard} from "./core/guard/authentication.guard";

export const routes: Routes = [
  {
    path: 'authentication',
    canActivate: [authGuard],
    loadChildren: () => import('./feature/authentication/authentication.module').then(m => m.AuthenticationModule),
    data: {
      jwtTokenPresent: false,
      redirectUrl: '/dashboard/clients'
    }
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadChildren: () => import('./feature/dashboard/dashboard.module').then(m => m.DashboardModule),
    data: {
      jwtTokenPresent: true,
      redirectUrl: '/authentication/login'
    }
  },
  {
    path: '**',
    redirectTo: 'authentication/login'
  },
];
