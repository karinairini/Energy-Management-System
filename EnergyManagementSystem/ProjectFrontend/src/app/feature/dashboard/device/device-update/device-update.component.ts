import {Component, DestroyRef, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {DeviceService} from "../../../../core/service/device/device.service";
import {Router} from "@angular/router";
import {ClientModel} from "../../../../shared/models/client.model";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {DeviceModel} from "../../../../shared/models/device.model";

@Component({
  selector: 'app-device-update',
  templateUrl: './device-update.component.html',
  styleUrl: './device-update.component.scss'
})
export class DeviceUpdateComponent implements OnInit {

  updateForm: FormGroup = new FormGroup({});
  deviceId: string = "";
  deviceDescription: string = "";
  deviceAddress: string = "";
  deviceConsumption: number = 0;
  deviceUser: ClientModel = {id: "", name: "", age: 0, email: "", role: ""};

  constructor(
    private deviceService: DeviceService,
    private formBuilder: FormBuilder,
    private destroyRef: DestroyRef,
    private router: Router) {
  }

  private buildUpdateForm(): void {
    this.updateForm = this.formBuilder.group({
      description: '',
      address: '',
      consumption: null
    });
  }

  private populateFromHistoryState(): void {
    this.deviceId = history.state.id;
    this.deviceDescription = history.state.description;
    this.deviceAddress = history.state.address;
    this.deviceConsumption = history.state.maximumHourlyConsumption;
    this.deviceUser = history.state.user;
  }

  ngOnInit() {
    this.buildUpdateForm();
    this.populateFromHistoryState();
  }

  updateDevice(): void {
    const updatedDevice: DeviceModel = {
      id: this.deviceId,
      description: this.updateForm?.get('description')?.value || this.deviceDescription,
      address: this.updateForm?.get('address')?.value || this.deviceAddress,
      maximumHourlyConsumption: this.updateForm?.get('consumption')?.value || this.deviceConsumption,
      user: this.deviceUser
    };

    this.deviceService.updateDevice(updatedDevice)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.router.navigateByUrl("/dashboard/clients");
      });
  }
}
