import { HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import {Pagination} from "../../core/request/request.model";

@Injectable({providedIn: 'root'})
export abstract class IService<T> {
  abstract query(req?: Pagination): Observable<HttpResponse<T[]>>
}
