import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IAuthor } from "./author.model";

@Injectable({ providedIn: 'root' })
export class AuthorService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/author');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  query(): Observable<HttpResponse<IAuthor[]>> {
    return this.http.get<IAuthor[]>(this.resourceUrl, { observe: 'response' });
  }

  find(id: number): Observable<HttpResponse<IAuthor>> {
    return this.http.get<IAuthor>(`${this.resourceUrl}/${id}`, { observe: 'response' })
  }

  create(author: IAuthor): Observable<HttpResponse<IAuthor>> {
    return this.http.post<IAuthor>(this.resourceUrl, author, { observe: 'response' });
  }

  update(author: IAuthor): Observable<HttpResponse<IAuthor>> {
    return this.http.put<IAuthor>(this.resourceUrl, author, { observe: 'response' });
  }
}
