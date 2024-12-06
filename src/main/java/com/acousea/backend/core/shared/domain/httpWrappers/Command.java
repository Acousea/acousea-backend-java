package com.acousea.backend.core.shared.domain.httpWrappers;

import com.acousea.backend.core.shared.domain.exceptions.PreviousUnresolvedRequestException;

import java.util.Arrays;

public abstract class Command<QueryParams, QueryResult> {

    public abstract Result<QueryResult> execute(QueryParams params);

    public Result<QueryResult> run(QueryParams params) {
        try {
            return execute(params);
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException: " + e.getMessage());
            return Result.fail(e.getMessage());
        } catch (PreviousUnresolvedRequestException e) {
            System.out.println("PreviousUnresolvedRequestException: " + e.getMessage());
            return Result.fail(403, e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("NullPointerException: " + e.getMessage());
            return Result.fail(404, e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            System.out.println("-> "+ e.getCause());
            System.out.println("-> " + e.getMessage());
            System.out.println("-> " + Arrays.toString(e.getStackTrace()));
            return Result.fail(-1, e.getMessage());
        }
    }
}
