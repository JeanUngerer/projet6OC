import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import {JWTService} from "./jwt.service";

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  pipe(arg0: any) {
    throw new Error('Method not implemented.');
  }
  constructor(private http: HttpClient,
              private jwtSerice: JWTService) {}

  private formatErrors(error: any) {
    return throwError(error.error);
  }

  get(path: string): Observable<any> {
    return this.http
      .get(`${environment.apiUrl}${path}`,
{
        headers: new HttpHeaders(
      {
        'Authorization': this.jwtSerice.getToken() ? "Bearer " + this.jwtSerice.getToken() : ""
        })
      })
      .pipe(catchError(this.formatErrors));
  }

  getNoAuth(path: string): Observable<any> {
    return this.http
      .get(`${environment.apiUrl}${path}`,
        {
          headers: new HttpHeaders(
            {
            })
        })
      .pipe(catchError(this.formatErrors));
  }

  put(path: string, body: Object = {}): Observable<any> {
    return this.http
      .put(
        `${environment.apiUrl}${path}`,
        JSON.stringify(body),
        {
          headers: new HttpHeaders(
            {
              'Authorization': "Bearer " + this.jwtSerice.getToken(),
              'content-type': 'application/json',
            })
        })
      .pipe(catchError(this.formatErrors));
  }

  putNoAuth(path: string, body: Object = {}): Observable<any> {
    return this.http
      .put(
        `${environment.apiUrl}${path}`,
        JSON.stringify(body),
        {
          headers: new HttpHeaders(
            {
              'content-type': 'application/json',
            })
        })
      .pipe(catchError(this.formatErrors));
  }

  post(path: string, body: Object = {}): Observable<any> {
    return this.http
      .post(
        `${environment.apiUrl}${path}`,
        JSON.stringify(body),
        {
          headers: new HttpHeaders(
            {
              'Authorization': "Bearer " + this.jwtSerice.getToken(),
              'content-type': 'application/json',
            })
        })
      .pipe(catchError(this.formatErrors));
  }

  delete(path: string): Observable<any> {
    return this.http
      .delete(`${environment.apiUrl}${path}`)
      .pipe(catchError(this.formatErrors));
  }

  postFile(fileToUpload: File, path: String): Observable<string> {
    const formData: FormData = new FormData();
    formData.append('file', fileToUpload, fileToUpload.name);
    return this.http
      .post(`${environment.apiUrl}${path}`, formData, {
        headers: { enctype: 'multipart/form-data' },
        responseType: 'text',
      })
      .pipe(catchError(this.formatErrors));
  }
}
