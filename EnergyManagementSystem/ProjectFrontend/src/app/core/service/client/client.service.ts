import {DestroyRef, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {ClientModel} from "../../../shared/models/client.model";
import {ClientRequestModel} from "../../../shared/models/client-request.model";

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  constructor(
    private http: HttpClient,
    private destroyRef: DestroyRef) {
  }

  getAll(): Observable<ClientModel[]> {
    return this.http.get<ClientModel[]>("user")
      .pipe(takeUntilDestroyed(this.destroyRef));
  }

  saveClient(client: ClientRequestModel): Observable<ClientModel> {
    return this.http.post<ClientModel>(`user`, client)
      .pipe(takeUntilDestroyed(this.destroyRef),
        map((response: ClientModel) => response));
  }

  updateClient(id: string, client: ClientRequestModel): Observable<ClientModel> {
    return this.http.put<ClientModel>(`user/${id}`, client)
      .pipe(takeUntilDestroyed(this.destroyRef),
        map((response: ClientModel) => response));
  }

  deleteClient(id: string): Observable<void> {
    return this.http.delete<void>(`user/${id}`)
      .pipe(takeUntilDestroyed(this.destroyRef));
  }

  getAdmins(): Observable<ClientModel[]> {
    return this.http.get<ClientModel[]>("user/admins")
      .pipe(takeUntilDestroyed(this.destroyRef));
  }
}
