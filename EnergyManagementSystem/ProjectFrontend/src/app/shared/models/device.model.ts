import {ClientModel} from "./client.model";

export interface DeviceModel {
  id: string;
  description: string;
  address: string;
  maximumHourlyConsumption: number;
  user: ClientModel;
}
