import {NgModule} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {SharedModule} from "../../shared/shared.module";
import {DashboardRoutingModule} from "./dashboard-routing.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ClientsComponent} from './clients/clients.component';
import {ClientCardComponent} from './clients/client-card/client-card.component';
import {NavigationBarComponent} from './navigation-bar/navigation-bar.component';
import {DeviceComponent} from './device/device.component';
import {DeviceCardComponent} from "./device/device-card/device-card.component";
import {DeviceSaveComponent} from './device/device-save/device-save.component';
import {DeviceUpdateComponent} from './device/device-update/device-update.component';
import {ClientUpdateComponent} from './clients/client-update/client-update.component';
import {IgxCalendarComponent, IgxDatePickerComponent} from "igniteui-angular";
import {AlertComponent} from './alert/alert.component';
import {ChatComponent} from "./chat/chat.component";

@NgModule({
  declarations: [
    NavigationBarComponent,
    ClientsComponent,
    ClientCardComponent,
    DeviceCardComponent,
    DeviceComponent,
    DeviceSaveComponent,
    DeviceUpdateComponent,
    ClientUpdateComponent,
    AlertComponent,
    ChatComponent
  ],
  imports: [
    CommonModule,
    SharedModule,
    DashboardRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    NgOptimizedImage,
    IgxCalendarComponent,
    IgxDatePickerComponent
  ]
})
export class DashboardModule {
}
