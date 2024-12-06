package com.acousea.backend.core.shared.domain.httpWrappers;

import com.acousea.backend.core.communicationSystem.domain.exceptions.InvalidPacketException;
import com.acousea.backend.core.shared.domain.exceptions.PreviousUnresolvedRequestException;

import java.util.Arrays;

public abstract class Command<QueryParams, QueryResult> {

    public abstract ApiResult<QueryResult> execute(QueryParams params) throws InvalidPacketException;

    public ApiResult<QueryResult> run(QueryParams params) {
        try {
            return execute(params);
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException: " + e.getMessage());
            return ApiResult.fail(e.getMessage());
        } catch (PreviousUnresolvedRequestException e) {
            System.out.println("PreviousUnresolvedRequestException: " + e.getMessage());
            return ApiResult.fail(403, e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("NullPointerException: " + e.getMessage());
            return ApiResult.fail(404, e.getMessage());
        } catch (InvalidPacketException e) {
            System.out.println("InvalidPacketException: " + e.getMessage());
            return ApiResult.fail(422, e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            System.out.println("-> "+ e.getCause());
            System.out.println("-> " + e.getMessage());
            System.out.println("-> " + Arrays.toString(e.getStackTrace()));
            return ApiResult.fail(-1, e.getMessage());
        }
    }
}
