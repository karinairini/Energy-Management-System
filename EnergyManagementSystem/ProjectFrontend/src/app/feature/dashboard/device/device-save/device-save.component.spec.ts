import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeviceSaveComponent } from './device-save.component';

describe('DeviceSaveComponent', () => {
  let component: DeviceSaveComponent;
  let fixture: ComponentFixture<DeviceSaveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DeviceSaveComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DeviceSaveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
