import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.scss']
})
export class AlertComponent implements OnInit {

  private _message: string = '';
  isVisible: boolean = false;

  @Input()
  set message(value: string) {
    this._message = value;
    this.isVisible = !!value;
  }

  get message(): string {
    return this._message;
  }

  constructor() {
  }

  ngOnInit(): void {
  }

  closeAlert(): void {
    this.isVisible = false;
  }
}
