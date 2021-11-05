package de.uniReddit.uniReddit.Exceptions;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceExistsException extends RuntimeException implements GraphQLError {
    private Map<String, Object> extensions = new HashMap<>();
    public ResourceExistsException(Object[] queryParams){
        super("Entry already exists!");
        for (int i = 0; i<queryParams.length; i++) {
            extensions.put("param" + i, queryParams[i]);
        }
    }
    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.ValidationError;
    }

    @Override
    public Map<String, Object> getExtensions() {
        return extensions;
    }
}
