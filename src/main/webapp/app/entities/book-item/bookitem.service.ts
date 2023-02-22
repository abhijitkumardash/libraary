import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ApplicationConfigService} from 'app/core/config/application-config.service';
import {BookItem, IBookItem} from "./bookitem.model";
import {BookSearchWithPagination} from "../../core/request/request.model";
import {createRequestOption} from "../../core/request/request-util";
import {IService} from "../../shared/service/iservice";

@Injectable({providedIn: 'root'})
export class BookItemService extends IService<BookItem> {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/book/items');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {
    super();}

  query(req?: BookSearchWithPagination): Observable<HttpResponse<IBookItem[]>> {
    const options = createRequestOption(req);
    return this.http.get<IBookItem[]>(this.resourceUrl, {params: options, observe: 'response'});
  }

  find(id: string): Observable<IBookItem> {
    return this.http.get<IBookItem>(`${this.resourceUrl}/${id}`)
  }

  create(bookItem: IBookItem): Observable<HttpResponse<IBookItem>> {
    return this.http.post<IBookItem>(this.resourceUrl, bookItem, {observe: 'response'});
  }

  update(bookItem: IBookItem): Observable<HttpResponse<IBookItem>> {
    return this.http.put<IBookItem>(`${this.resourceUrl}/${bookItem.id}`, bookItem, {observe: 'response'});
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, {observe: 'response'});
  }
}
