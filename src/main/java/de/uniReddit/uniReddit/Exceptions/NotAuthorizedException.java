package de.uniReddit.uniReddit.Exceptions;

import de.uniReddit.uniReddit.Models.Node;
import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotAuthorizedException extends RuntimeException implements GraphQLError {
    private Map<String, Object> extensions = new HashMap<>();
    public NotAuthorizedException(Long nodeId){
        super("Not authorization to access record!");
        extensions.put("node", nodeId);
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
