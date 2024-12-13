import {ClientModel} from "./client.model";

export interface DeviceRequestModel {
  description: string;
  address: string;
  maximumHourlyConsumption: number;
  user: ClientModel;
}
