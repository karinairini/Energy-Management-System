import {inject} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivateFn, Router} from '@angular/router';
import {UserModel} from '../../shared/models/user.model';
import {getUser} from "../service/user/user.service";

export const hasRole: CanActivateFn = (route: ActivatedRouteSnapshot) => {

  const router: Router = inject(Router);
  const requiredRoles: string[] = route.data['requiredRoles'];
  const loggedUser: UserModel = getUser();

  if (requiredRoles.includes(loggedUser.role)) {
    return true;
  }

  if (loggedUser.role === "CLIENT") {
    return router.navigateByUrl(`/dashboard/devices`);
  }

  return router.navigateByUrl(`/dashboard/clients`);
};
