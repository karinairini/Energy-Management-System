import {Component, DestroyRef, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {ClientService} from "../../../../core/service/client/client.service";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {ClientRequestModel} from "../../../../shared/models/client-request.model";

@Component({
  selector: 'app-client-update',
  templateUrl: './client-update.component.html',
  styleUrl: './client-update.component.scss'
})
export class ClientUpdateComponent implements OnInit {

  updateForm: FormGroup = new FormGroup({});
  clientId: string = "";
  clientName: string = "";
  clientAge: number = 0;
  clientEmail: string = "";

  constructor(
    private clientService: ClientService,
    private formBuilder: FormBuilder,
    private destroyRef: DestroyRef,
    private router: Router) {
  }

  private buildUpdateForm(): void {
    this.updateForm = this.formBuilder.group({
      name: '',
      age: [null, [Validators.required, Validators.min(18)]],
      email: '',
      password: ''
    });
  }

  private populateFromHistoryState(): void {
    this.clientId = history.state.id;
    this.clientName = history.state.name;
    this.clientAge = history.state.age;
    this.clientEmail = history.state.email;
  }

  ngOnInit() {
    this.buildUpdateForm();
    this.populateFromHistoryState();
  }

  updateClient(): void {
    const updatedClient: ClientRequestModel = {
      name: this.updateForm.get('name')?.value || this.clientName,
      age: this.updateForm.get('age')?.value || this.clientAge,
      email: this.updateForm.get('email')?.value || this.clientEmail,
      password: this.updateForm.get('password')?.value
    };

    this.clientService.updateClient(this.clientId, updatedClient)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        this.router.navigateByUrl("/dashboard/clients");
      });
  }
}
