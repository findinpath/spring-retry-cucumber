package cucumber.api.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import io.cucumber.core.api.TypeRegistry;
import io.cucumber.core.api.TypeRegistryConfigurer;
import io.cucumber.cucumberexpressions.ParameterByTypeTransformer;
import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.datatable.TableCellByTypeTransformer;
import io.cucumber.datatable.TableEntryByTypeTransformer;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;

/**
 * Utility class which adds the ability for parsing via Jackson parser the data object parameters
 * for the cucumber BDD methods.
 */
public class ParameterTypes implements TypeRegistryConfigurer {

  @Override
  public Locale locale() {
    return Locale.ENGLISH;
  }

  @Override
  public void configureTypeRegistry(TypeRegistry typeRegistry) {
    var transformer = new Transformer();
    typeRegistry.setDefaultDataTableCellTransformer(transformer);
    typeRegistry.setDefaultDataTableEntryTransformer(transformer);
    typeRegistry.setDefaultParameterTransformer(transformer);
    typeRegistry.defineParameterType(new ParameterType<>(
        "requestMethod", // name
        "GET|POST|PUT|DELETE", // regexp
        RequestMethod.class, // type
        (io.cucumber.cucumberexpressions.Transformer<RequestMethod>) s -> {
          RequestMethod requestMethod;
          if ("GET".equals(s)) {
            requestMethod = RequestMethod.GET;
          } else if ("POST".equals(s)) {
            requestMethod = RequestMethod.POST;
          } else if ("PUT".equals(s)) {
            requestMethod = RequestMethod.PUT;
          } else if ("DELETE".equals(s)) {
            requestMethod = RequestMethod.DELETE;
          } else {
            throw new IllegalArgumentException("Unknown value " + s + " for RequestMethod");
          }
          return requestMethod;
        }
    ));

    var regexpHttpStatus = Arrays.stream(HttpStatus.values()).map(Enum::name)
        .collect(Collectors.joining("|"));
    typeRegistry.defineParameterType(new ParameterType<>(
        "httpStatus", // name
        regexpHttpStatus, // regex
        HttpStatus.class, // type
        (io.cucumber.cucumberexpressions.Transformer<HttpStatus>) HttpStatus::valueOf
    ));

  }


  private static class Transformer
      implements ParameterByTypeTransformer, TableEntryByTypeTransformer,
      TableCellByTypeTransformer {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object transform(String s, Type type) {
      return objectMapper.convertValue(s, objectMapper.constructType(type));
    }

    @Override
    public <T> T transform(Map<String, String> map,
        Class<T> aClass,
        TableCellByTypeTransformer tableCellByTypeTransformer
    ) {
      return objectMapper.convertValue(map, aClass);
    }

    public <T> T transform(String s, Class<T> aClass) {
      return objectMapper.convertValue(s, aClass);
    }
  }
}