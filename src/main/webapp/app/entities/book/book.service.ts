import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import {IBook} from "./book.model";

@Injectable({ providedIn: 'root' })
export class BookService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/book');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  query(): Observable<HttpResponse<IBook[]>> {
    return this.http.get<IBook[]>(this.resourceUrl, { observe: 'response' });
  }

  find(id: number): Observable<HttpResponse<IBook>> {
    return this.http.get<IBook>(`${this.resourceUrl}/${id}`, { observe: 'response' })
  }

  create(book: IBook): Observable<HttpResponse<IBook>> {
    return this.http.post<IBook>(this.resourceUrl, book, { observe: 'response' });
  }

  update(book: IBook): Observable<HttpResponse<IBook>> {
    return this.http.put<IBook>(this.resourceUrl, book, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}

