import {AfterViewInit, Component, ElementRef, EventEmitter, Input, OnDestroy, Output, ViewChild} from '@angular/core';
import {DeviceModel} from "../../../../shared/models/device.model";
import {Router} from "@angular/router";
import {DeviceDataService} from "../../../../core/service/device-data/device-data.service";
import {Chart, registerables} from 'chart.js';

Chart.register(...registerables);

@Component({
  selector: 'app-device-card',
  templateUrl: './device-card.component.html',
  styleUrls: ['./device-card.component.scss']
})
export class DeviceCardComponent implements OnDestroy, AfterViewInit {

  @Input() device!: DeviceModel;
  @Output() deleteDeviceEvent: EventEmitter<string> = new EventEmitter<string>();
  @Input() isAdmin!: boolean;
  @ViewChild('deviceChart', {static: false}) chartCanvas!: ElementRef;
  showCalendar: boolean = false;
  showChart: boolean = false;
  chart!: Chart;
  deviceDataForDay: { [deviceId: string]: number[] } = {};
  public date: string;
  numberOfElements: number = 0;

  constructor(
    private deviceDataService: DeviceDataService,
    private router: Router) {
    this.date = this.getCurrentDate();
  }

  ngAfterViewInit(): void {
  }

  getCurrentDate(): string {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    return `${day}-${month}-${year}`;
  }

  deleteDevice(): void {
    this.deleteDeviceEvent.emit(this.device.id);
  }

  updateDevice(): void {
    this.router.navigate(['/dashboard/updateDevice'], {
      state: {
        id: this.device.id,
        description: this.device.description,
        address: this.device.address,
        maximumHourlyConsumption: this.device.maximumHourlyConsumption,
        user: this.device.user
      }
    });
  }

  toggleShowCalendar(): void {
    this.showCalendar = !this.showCalendar;
  }

  toggleShowChart(): void {
    this.showChart = !this.showChart;
    if (!this.showChart && this.chart) {
      this.chart.destroy();
    }
  }

  fetchDataForDate(): void {
    const [day, month, year] = this.date.split('-').map(Number);
    const formattedDate = `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}`;

    this.deviceDataService.getDeviceDataValuesForDay(this.device.id, formattedDate).subscribe(dataValues => {
      this.deviceDataForDay[this.device.id] = dataValues;
      const labels = Array.from({length: dataValues.length}, (_, i) => `${i}:00`);
      this.showChart = true;
      this.numberOfElements = Object.keys(this.deviceDataForDay[this.device.id]).length;
      this.initializeChart(labels, dataValues);
    });

    this.toggleShowCalendar();
  }

  initializeChart(labels: string[], data: number[]): void {
    if (this.chart) this.chart.destroy();

    setTimeout(() => {
      if (!this.chartCanvas || !this.chartCanvas.nativeElement) return;

      const canvas = this.chartCanvas.nativeElement as HTMLCanvasElement;

      const fontSize = 27;
      const fontConfig = {font: {size: fontSize}};

      this.chart = new Chart(canvas, {
        type: 'line',
        data: {
          labels: labels,
          datasets: [{
            label: 'Device Consumption (kW)',
            data: data,
            borderColor: 'rgba(75, 192, 192, 1)',
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            fill: true,
            tension: 0.3
          }]
        },
        options: {
          responsive: true,
          plugins: {
            legend: {labels: fontConfig},
            tooltip: {
              titleFont: fontConfig.font,
              bodyFont: fontConfig.font
            }
          },
          scales: {
            x: {
              title: {display: true, text: 'Hour of Day', ...fontConfig},
              ticks: fontConfig
            },
            y: {
              title: {display: true, text: 'Consumption (kW)', ...fontConfig},
              ticks: fontConfig
            }
          }
        }
      });
    }, 0);
  }

  ngOnDestroy(): void {
    if (this.chart) {
      this.chart.destroy();
    }
  }
}
