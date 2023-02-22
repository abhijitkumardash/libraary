import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import {IBook} from "./book.model";
import { ICategory } from "./category.model";
import { ISubCategory } from "./subcategory.model";

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

  findByIsbn(isbn: string): Observable<HttpResponse<IBook>> {
    return this.http.get<IBook>(`${this.resourceUrl}/isbn/${isbn}`, { observe: 'response' })
  }

  searchByISBN(isbn: string): Observable<HttpResponse<IBook>> {
    return this.http.get<IBook>(`${this.resourceUrl}/search/isbn/${isbn}`, { observe: 'response' })
  }

  create(book: IBook): Observable<HttpResponse<IBook>> {
    return this.http.post<IBook>(this.resourceUrl, book, { observe: 'response' });
  }

  update(book: IBook): Observable<HttpResponse<IBook>> {
    return this.http.put<IBook>(`${this.resourceUrl}/${book.id}`, book, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  queryCategories(): Observable<HttpResponse<ICategory[]>> {
    return this.http.get<ICategory[]>(`${this.resourceUrl}/category`, { observe: 'response' });
  }

  querySubCategories(category: string): Observable<HttpResponse<ISubCategory[]>> {
    return this.http.get<ISubCategory[]>(`${this.resourceUrl}/category/${category}`, { observe: 'response' });
  }

  querySubCategoriesByCategory(id: number): Observable<HttpResponse<ISubCategory[]>> {
    return this.http.get<ISubCategory[]>(`${this.resourceUrl}/category/${id}/subcategory`, { observe: 'response' });
  }

  createSubCategory(subCategory: ISubCategory): Observable<HttpResponse<ISubCategory>> {
    return this.http.post<ISubCategory>(`${this.resourceUrl}/subcategory`, subCategory, { observe: 'response' });
  }

  updateSubCategory(subCategory: ISubCategory): Observable<HttpResponse<ISubCategory>> {
    return this.http.put<ISubCategory>(`${this.resourceUrl}/subcategory`, subCategory, { observe: 'response' });
  }

  deleteSubCategory(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/subcategory/${id}`, { observe: 'response' });
  }

  createCategory(category: ICategory): Observable<HttpResponse<ICategory>> {
    return this.http.post<ICategory>(`${this.resourceUrl}/category`, category, { observe: 'response' });
  }

  updateCategory(category: ICategory): Observable<HttpResponse<ICategory>> {
    return this.http.put<ICategory>(`${this.resourceUrl}/category`, category, { observe: 'response' });
  }

  deleteCategory(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/category/${id}`, { observe: 'response' });
  }
}

