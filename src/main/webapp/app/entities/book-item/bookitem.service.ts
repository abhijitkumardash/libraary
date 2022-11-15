import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ApplicationConfigService} from 'app/core/config/application-config.service';
import {IBookItem} from "./bookitem.model";

@Injectable({providedIn: 'root'})
export class BookItemService {
  private resourceUrl = this .applicationConfigService.getEndpointFor('api/book-item');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  query(): Observable<HttpResponse<IBookItem[]>> {
    return this.http.get<IBookItem[]>(this.resourceUrl, {observe: 'response'});
  }

  find(id: number): Observable<HttpResponse<IBookItem>> {
    return this.http.get<IBookItem>(`${this.resourceUrl}/${id}`, {observe: 'response'})
  }

  create(bookItem: IBookItem): Observable<HttpResponse<IBookItem>> {
    return this.http.post<IBookItem>(this.resourceUrl, bookItem, {observe: 'response'});
  }

  update(bookItem: IBookItem): Observable<HttpResponse<IBookItem>> {
    return this.http.put<IBookItem>(this.resourceUrl, bookItem, {observe: 'response'});
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, {observe: 'response'});
  }
}
