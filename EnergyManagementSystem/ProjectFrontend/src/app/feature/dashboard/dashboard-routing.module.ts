import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {NotFoundComponent} from "../../shared/components/not-found/not-found.component";
import {InvalidAccessComponent} from "../../shared/components/invalid-access/invalid-access.component";
import {ClientsComponent} from "./clients/clients.component";
import {hasRole} from "../../core/guard/authorization.guard";
import {DeviceSaveComponent} from "./device/device-save/device-save.component";
import {DeviceUpdateComponent} from "./device/device-update/device-update.component";
import {ClientUpdateComponent} from "./clients/client-update/client-update.component";
import {DeviceComponent} from "./device/device.component";
import {ChatComponent} from "./chat/chat.component";

export const routes: Routes = [
  {
    path: 'devices',
    component: DeviceComponent,
  },
  {
    path: 'newDevice',
    component: DeviceSaveComponent,
    canActivate: [hasRole],
    data: {
      requiredRoles: ['ADMIN']
    }
  },
  {
    path: 'updateDevice',
    component: DeviceUpdateComponent,
    canActivate: [hasRole],
    data: {
      requiredRoles: ['ADMIN']
    }
  },
  {
    path: 'clients',
    component: ClientsComponent,
    canActivate: [hasRole],
    data: {
      requiredRoles: ['ADMIN']
    }
  },
  {
    path: 'updateClient',
    component: ClientUpdateComponent,
    canActivate: [hasRole],
    data: {
      requiredRoles: ['ADMIN']
    }
  },
  {
    path: 'chat',
    component: ChatComponent,
    canActivate: [hasRole],
    data: {
      requiredRoles: ['ADMIN', 'CLIENT']
    }
  },
  {
    path: 'invalid-access',
    component: InvalidAccessComponent
  },
  {
    path: '**',
    component: NotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule {
}
