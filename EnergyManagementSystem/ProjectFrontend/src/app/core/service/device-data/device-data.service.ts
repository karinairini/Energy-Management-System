import {DestroyRef, Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";

@Injectable({
  providedIn: 'root'
})
export class DeviceDataService {

  constructor(
    private http: HttpClient,
    private destroyRef: DestroyRef) {
  }

  getDeviceDataValuesForDay(deviceId: string, date: string): Observable<number[]> {
    const params = new HttpParams().set('date', date);
    return this.http.get<number[]>(`device-data/${deviceId}/values/day`, {params})
      .pipe(takeUntilDestroyed(this.destroyRef),
        map((response: number[]) => response));
  }
}
