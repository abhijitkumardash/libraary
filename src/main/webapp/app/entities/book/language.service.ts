import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { ILanguage } from "./language.model";

@Injectable({ providedIn: 'root' })
export class LanguageService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/language');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  query(): Observable<HttpResponse<ILanguage[]>> {
    return this.http.get<ILanguage[]>(this.resourceUrl, { observe: 'response' });
  }

  find(id: number): Observable<HttpResponse<ILanguage>> {
    return this.http.get<ILanguage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
  }
}
