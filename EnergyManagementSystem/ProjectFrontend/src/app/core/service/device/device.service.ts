import {DestroyRef, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";
import {DeviceModel} from "../../../shared/models/device.model";
import {DeviceRequestModel} from "../../../shared/models/device-request.model";

@Injectable({
  providedIn: 'root'
})
export class DeviceService {

  constructor(
    private http: HttpClient,
    private destroyRef: DestroyRef) {
  }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt-token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getAllByClientId(userId: string): Observable<DeviceModel[]> {
    return this.http.get<DeviceModel[]>(`device/user/${userId}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      takeUntilDestroyed(this.destroyRef),
      map((response: DeviceModel[]) => response)
    );
  }

  saveDevice(device: DeviceRequestModel): Observable<DeviceModel> {
    return this.http.post<DeviceModel>(`device/${device.user.id}`, device, {
      headers: this.getAuthHeaders()
    }).pipe(
      takeUntilDestroyed(this.destroyRef),
      map((response: DeviceModel) => response)
    );
  }

  updateDevice(device: DeviceModel): Observable<DeviceModel> {
    return this.http.put<DeviceModel>(`device/${device.id}`, device, {
      headers: this.getAuthHeaders()
    }).pipe(
      takeUntilDestroyed(this.destroyRef),
      map((response: DeviceModel) => response)
    );
  }

  deleteDevice(id: string): Observable<void> {
    return this.http.delete<void>(`device/${id}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      takeUntilDestroyed(this.destroyRef)
    );
  }
}
