import {Component, DestroyRef, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {DeviceService} from "../../../../core/service/device/device.service";
import {Router} from "@angular/router";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {ClientModel} from "../../../../shared/models/client.model";
import {DeviceRequestModel} from "../../../../shared/models/device-request.model";

@Component({
  selector: 'app-device-save',
  templateUrl: './device-save.component.html',
  styleUrl: './device-save.component.scss'
})
export class DeviceSaveComponent implements OnInit {

  saveForm: FormGroup = new FormGroup({});
  errorMessage?: string;
  clientId: string = "";

  constructor(
    private deviceService: DeviceService,
    private formBuilder: FormBuilder,
    private destroyRef: DestroyRef,
    private router: Router) {
  }

  private buildSaveForm(): void {
    this.saveForm = this.formBuilder.group({
      description: ['', [Validators.required]],
      address: ['', [Validators.required]],
      consumption: [null, [Validators.required, Validators.min(0)]],
    });
  }

  ngOnInit() {
    this.buildSaveForm();
    this.clientId = history.state.id;
  }

  private createUser(): ClientModel {
    return {
      id: this.clientId,
      name: "",
      age: 0,
      email: "",
      role: ""
    };
  }

  saveDevice(): void {
    if (!this.saveForm?.valid) {
      this.errorMessage = 'Invalid form completion!';
      setTimeout(() => this.errorMessage = undefined, 3000);
      return;
    }

    const device: DeviceRequestModel = {
      description: this.saveForm?.get('description')?.value,
      address: this.saveForm?.get('address')?.value,
      maximumHourlyConsumption: this.saveForm?.get('consumption')?.value,
      user: this.createUser()
    };

    this.deviceService.saveDevice(device)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.router.navigateByUrl("/dashboard/clients");
      });
  }
}
