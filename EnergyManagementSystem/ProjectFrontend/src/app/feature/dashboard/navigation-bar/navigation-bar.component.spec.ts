import {ComponentFixture, TestBed} from '@angular/core/testing';

import {NavigationBarComponent} from './navigation-bar.component';
import {Router} from "@angular/router";
import {HttpClientModule} from "@angular/common/http";
import {CommonModule} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

describe('NavigationBarComponent', () => {
  let component: NavigationBarComponent;
  let fixture: ComponentFixture<NavigationBarComponent>;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientModule, CommonModule, FormsModule, ReactiveFormsModule],
      declarations: [NavigationBarComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(NavigationBarComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
  });

  it('should create', () => {
    //when
    fixture.detectChanges();

    //then
    expect(component).toBeTruthy();
  });

  it('should logout when button is pressed', () => {
    //given
    fixture.detectChanges();
    const button: HTMLButtonElement = fixture.debugElement.nativeElement.querySelector('#logout-button');
    const buttonSpy = spyOn(component as any, 'logOut').and.callThrough();
    const routerSpy = spyOn(router as any, 'navigateByUrl');

    //when
    button.click();

    //then
    expect(buttonSpy).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith('/authentication/login' as any);
  });

  it('should display employees when button is pressed', () => {
    //given
    fixture.detectChanges();
    const button: HTMLButtonElement = fixture.debugElement.nativeElement.querySelector('#employees-button');
    const buttonSpy = spyOn(component as any, 'goToEmployees').and.callThrough();
    const routerSpy = spyOn(router as any, 'navigateByUrl');

    //when
    button.click();

    //then
    expect(buttonSpy).toHaveBeenCalled();
    expect(routerSpy).toHaveBeenCalledWith('/dashboard/employees' as any);
  });
});
