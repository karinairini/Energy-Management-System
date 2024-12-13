import {DeviceModel} from "./device.model";

export interface DeviceDataModel {
  id: string,
  value: number,
  timestamp: string,
  device: DeviceModel
}
