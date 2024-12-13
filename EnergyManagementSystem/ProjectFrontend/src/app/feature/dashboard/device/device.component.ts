import {Component, DestroyRef, OnDestroy, OnInit} from '@angular/core';
import {BehaviorSubject, interval, withLatestFrom} from 'rxjs'; // Correct import
import {DeviceModel} from "../../../shared/models/device.model";
import {UserModel} from "../../../shared/models/user.model";
import {getUser} from "../../../core/service/user/user.service";
import {DeviceService} from "../../../core/service/device/device.service";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-device',
  templateUrl: './device.component.html',
  styleUrls: ['./device.component.scss']
})
export class DeviceComponent implements OnInit, OnDestroy {

  devices: DeviceModel[] = [];
  loggedUser: UserModel = getUser();
  private socket: WebSocket | undefined;
  alertMessage$: BehaviorSubject<string> = new BehaviorSubject<string>('');
  alertMessage: string = '';

  constructor(
    private deviceService: DeviceService,
    private destroyRef: DestroyRef) {
  }

  ngOnInit(): void {
    this.getDevices();
    this.connectWebSocket();
    this.setupPeriodicAlertDisplay();
  }

  ngOnDestroy(): void {
    this.socket?.close();
    this.alertMessage$.complete();
  }

  private getDevices(): void {
    this.deviceService.getAllByClientId(this.loggedUser.id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (response) => {
          this.devices = response;
        },
        error: (error) => console.log(error)
      });
  }

  private connectWebSocket(): void {
    this.socket = new WebSocket(`ws://localhost/api/energy-management-monitor/ws/notifications?userId=${this.loggedUser.id}`);

    this.socket.onopen = () => {
      console.log("WebSocket connection for alert established.");
    };

    this.socket.onmessage = (event) => {
      const message = JSON.parse(event.data);
      const formattedMessage = `Alert for device ${message.description}:\n
        ${message.alert}\n
        Value: ${message.value}\n
        Maximum Hourly Consumption: ${message.maximumHourlyConsumption}`;

      this.alertMessage$.next(formattedMessage);
    };

    this.socket.onclose = () => {
      console.log("WebSocket connection for alert closed.");
    };
  }

  private setupPeriodicAlertDisplay(): void {
    interval(10000)
      .pipe(withLatestFrom(this.alertMessage$), takeUntilDestroyed(this.destroyRef))
      .subscribe(([_, latestMessage]) => {
        this.alertMessage = latestMessage;
      });
  }
}
