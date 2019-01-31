import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IReaction } from 'app/shared/model/reaction.model';

type EntityResponseType = HttpResponse<IReaction>;
type EntityArrayResponseType = HttpResponse<IReaction[]>;

@Injectable({ providedIn: 'root' })
export class ReactionService {
    public resourceUrl = SERVER_API_URL + 'api/reactions';

    constructor(protected http: HttpClient) {}

    create(reaction: IReaction): Observable<EntityResponseType> {
        return this.http.post<IReaction>(this.resourceUrl, reaction, { observe: 'response' });
    }

    update(reaction: IReaction): Observable<EntityResponseType> {
        return this.http.put<IReaction>(this.resourceUrl, reaction, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IReaction>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IReaction[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
