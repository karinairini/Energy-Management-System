import {Component, DestroyRef, EventEmitter, Input, Output} from '@angular/core';
import {ClientModel} from "../../../../shared/models/client.model";
import {DeviceModel} from "../../../../shared/models/device.model";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {DeviceService} from "../../../../core/service/device/device.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-client-card',
  templateUrl: './client-card.component.html',
  styleUrl: './client-card.component.scss'
})
export class ClientCardComponent {

  @Input() client!: ClientModel;
  @Input() devices: DeviceModel[] = [];
  @Output() deleteClientEvent: EventEmitter<string> = new EventEmitter<string>();
  showDetails: boolean = false;

  constructor(
    private deviceService: DeviceService,
    private destroyRef: DestroyRef,
    private router: Router) {
  }

  toggleDeviceDetails(): void {
    this.showDetails = !this.showDetails;
  }

  trackByDeviceId(index: number, device: DeviceModel): string {
    return device.id;
  }

  updateClient(): void {
    this.router.navigate(['/dashboard/updateClient'], {
      state: {
        id: this.client.id,
        name: this.client.name,
        age: this.client.age,
        email: this.client.email,
      }
    });
  }

  deleteClient(): void {
    this.deleteClientEvent.emit(this.client.id);
  }

  addDevice(): void {
    this.router.navigate(['/dashboard/newDevice'], {
      state: {
        id: this.client.id
      }
    });
  }

  deleteDevice(id: string): void {
    this.deviceService.deleteDevice(id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.devices = this.devices.filter(
            device => device.id !== id
          );
        }
      });
  }
}
