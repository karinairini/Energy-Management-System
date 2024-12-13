import {Component, DestroyRef, OnInit} from '@angular/core';
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {ClientModel} from "../../../shared/models/client.model";
import {ClientService} from "../../../core/service/client/client.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {getUser} from "../../../core/service/user/user.service";
import {UserModel} from "../../../shared/models/user.model";
import {DeviceModel} from "../../../shared/models/device.model";
import {DeviceService} from "../../../core/service/device/device.service";
import {ClientRequestModel} from "../../../shared/models/client-request.model";

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrl: './clients.component.scss'
})
export class ClientsComponent implements OnInit {

  clients: ClientModel[] = [];
  clientDevicesMap: { [key: string]: DeviceModel[] } = {};
  showForm: boolean = false;
  saveForm: FormGroup = new FormGroup({});
  errorMessage?: string;
  loggedUser: UserModel = getUser();

  constructor(
    private clientService: ClientService,
    private deviceService: DeviceService,
    private formBuilder: FormBuilder,
    private destroyRef: DestroyRef) {
  }

  ngOnInit(): void {
    this.getClients();
    this.buildSaveForm();
  }

  toggleShowForm(): void {
    this.showForm = !this.showForm;
  }

  clearForm(): void {
    this.saveForm.reset();
  }

  private buildSaveForm(): void {
    this.saveForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      age: [null, [Validators.required, Validators.min(18)]],
      password: ['', [Validators.required]]
    });
  }

  private getClients(): void {
    this.clientService.getAll().pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (response) => {
          this.clients = response;
          this.clients.forEach(client => this.getDevicesByClientId(client.id));
        }
      });
  }

  private getDevicesByClientId(clientId: string): void {
    this.deviceService.getAllByClientId(clientId).pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (devices) => {
          this.clientDevicesMap[clientId] = devices;
        },
        error: (error) => console.log(error)
      });
  }

  saveClient(): void {
    const client: ClientRequestModel = {
      name: this.saveForm?.get('name')?.value,
      age: this.saveForm?.get('age')?.value,
      email: this.saveForm?.get('email')?.value,
      password: this.saveForm?.get('password')?.value
    };

    if (!this.saveForm?.valid) {
      this.errorMessage = 'Invalid form completion!';
      setTimeout(() => this.errorMessage = undefined, 3000);
      return;
    }

    this.clientService.saveClient(client)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (newClient: ClientModel) => {
          this.clients.push(newClient);
          this.toggleShowForm();
        }
      });
  }

  deleteClient(id: string): void {
    this.clientService.deleteClient(id)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.clients = this.clients.filter(
            client => client.id !== id
          );
        }
      });
  }
}
