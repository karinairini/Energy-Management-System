import {HttpHeaders, HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';

export const requestInterceptor: HttpInterceptorFn = (req, next) => {

  const modifiedReq = req.clone({
    url: getUrl(req.url),
    headers: getHeaders(req.url),
    withCredentials: true
  });

  return next(modifiedReq);
};

/*const getUrl = (url: string): string => {
  if (url.includes('device-data')) {
    return 'http://localhost:8082/api/energy-management/' + url;
  }
  else if (url.includes('device')) {
    return 'http://localhost:8081/api/energy-management/' + url;
  }
  return 'http://localhost:8080/api/energy-management/' + url;
}*/

const getUrl = (url: string): string => {
  if (url.includes('device-data')) {
    return 'http://localhost/api/energy-management-monitor/' + url;
  } else if (url.includes('device')) {
    return 'http://localhost/api/energy-management-device/' + url;
  }
  return 'http://localhost/api/energy-management-user/' + url;
}

const getHeaders = (url: string): HttpHeaders => {
  const cookieService = inject(CookieService);
  const jwtToken = cookieService.get('jwt-token');

  return url.includes('auth') || jwtToken === ''
    ? new HttpHeaders()
    : new HttpHeaders({'Authorization': `Bearer ${jwtToken}`});
};
