package com.acousea.backend.core.shared.domain;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class HttpRequest<QueryParams, QueryResult> {

    protected ObjectMapper objectMapper = new ObjectMapper();

    public abstract HttpResponse<QueryResult> execute(QueryParams params);

    public HttpResponse<QueryResult> run(QueryParams params) {
        try {
            return execute(params);
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException: " + e.getMessage());
            return HttpResponse.fail(e.getMessage());
        } catch (PreviousUnresolvedRequestException e) {
            System.out.println("PreviousUnresolvedRequestException: " + e.getMessage());
            return HttpResponse.fail(403, e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            return HttpResponse.fail(-1, e.getMessage());
        }
    }
}
