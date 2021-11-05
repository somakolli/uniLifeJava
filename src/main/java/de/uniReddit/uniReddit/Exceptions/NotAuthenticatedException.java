package de.uniReddit.uniReddit.Exceptions;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotAuthenticatedException extends RuntimeException implements GraphQLError{
    private Map<String, Object> extensions = new HashMap<>();
    public NotAuthenticatedException(){
        super("Not authenticated!");
    }
    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.ExecutionAborted;
    }
}
